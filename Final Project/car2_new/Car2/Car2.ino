const int pingPinL = 4; // Trigger Pin of Ultrasonic Sensor
const int echoPinL = 5; // Echo Pin of Ultrasonic Sensor
const int motorLF = 9;
const int motorLB = 6;
const int motorRF = 10;
const int motorRB = 11;
const int rightIR = 12;
const int leftIR = 7;
bool motion = false;

const int minSpeed = 40;
const int maxSpeed = 110;
int leftSensor = 0;
int rightSensor = 0;

char t;
int v1 = 40;
int v = 0;
int v_rotate_dif = 40;
int rotateBias = 6;
int target = 30;

const int distanceWindowSize = 10;
long distanceWindow[distanceWindowSize];

void setup() {
  pinMode(motorLF, OUTPUT);  //left motors forward
  pinMode(motorLB, OUTPUT);  //left motors reverse
  pinMode(motorRF, OUTPUT);  //right motors forward
  pinMode(motorRB, OUTPUT);  //right motors reverse
  pinMode(echoPinL, INPUT);
  pinMode(pingPinL, OUTPUT);
  pinMode(rightIR, INPUT);
  pinMode(leftIR, INPUT);
  pinMode(LED_BUILTIN, OUTPUT);

 // digitalWrite(leftIR, HIGH);
 // digitalWrite(rightIR, HIGH);

  for(int i = 0; i < distanceWindowSize; i++){
    distanceWindow[i] = 0;
  }

  Serial.begin(9600);

}

void loop() {

  //drive();

  //while(1){

     if(Serial.available()) {
      t = Serial.read();
      Serial.println(t);
      if (t == '1') {
        target = target + 20;   // turn the LED on (HIGH is the voltage level)                   // wait for a second
      }
      else if (t == '2') {
        target = target - 20;   // turn the LED on (HIGH is the voltage level)                     // wait for a second
      }
      else if(t == '3') {
        motion = true;
      }
      else { motion = false;
      } 
    }
    drive();
    
    leftSensor = digitalRead(leftIR);
    rightSensor = digitalRead(rightIR);
    
    if(digitalRead(leftIR) == 1 && digitalRead(rightIR) == 1){
      drive();
    }else if(digitalRead(leftIR) == 0 && digitalRead(rightIR) == 0){
      drive();
    }else if(digitalRead(leftIR) == 1 && digitalRead(rightIR) == 0){
      analogWrite(motorLF, 0);
      analogWrite(motorLB, 0);
      analogWrite(motorRF, getV());
      analogWrite(motorRB, 0);
      drive();
    }else if(digitalRead(rightIR) == 1 && digitalRead(leftIR) == 0){
      analogWrite(motorLF, getV());
      analogWrite(motorLB, 0);
      analogWrite(motorRF, 0);
      analogWrite(motorRB, 0);

      drive();
    }
    
  /*delay(100);*/
  
 // }
 }


long microsecondsToCentimeters(long microseconds) {
  return microseconds / 29 / 2;
}

void updateWindow(long cm){
  
  for(int i = distanceWindowSize - 1; i > 0; i--){
    distanceWindow[i] = distanceWindow[i - 1];
  }
  distanceWindow[0] = cm;
}

long meanOfWindow(){
  long sum = 0;
  for(int i = 0; i < distanceWindowSize; i++){
    sum += distanceWindow[i];
  }
  return sum / distanceWindowSize;
}

void removeOutlyingDataInWindow(int index){
  if(index <= 0 || index >= distanceWindowSize - 1 || distanceWindow[index + 1] == 0){
    return;
  }else{
    if(abs(distanceWindow[index] - distanceWindow[index - 1]) > 3 * distanceWindow[index - 1]){
      if(abs(distanceWindow[index] - distanceWindow[index + 1]) > 3 * distanceWindow[index + 1]){
        distanceWindow[index] = distanceWindow[index - 1];
      }
    }
  }
}

void drive() {
  v = getV();
  if(v != 0){
  analogWrite(motorLF, v + rotateBias);
  analogWrite(motorLB, 0);
  analogWrite(motorRF, max(v - rotateBias, 0));
  analogWrite(motorRB, 0);
  } else {
  analogWrite(motorLF, 0);
  analogWrite(motorLB, 0);
  analogWrite(motorRF, 0);
  analogWrite(motorRB, 0);
  }
}

long get_distance() {
  long duration, cm;
  digitalWrite(pingPinL, LOW);
  delayMicroseconds(2);
  digitalWrite(pingPinL, HIGH);
  delayMicroseconds(10);
  digitalWrite(pingPinL, LOW);
  duration = pulseIn(echoPinL, HIGH);
  cm = microsecondsToCentimeters(duration);
  updateWindow(cm);
  //removeOutlyingDataInWindow(1); // Check second element not to be an outlying data
  cm = meanOfWindow();
  return cm;
  delay(100);
}

int getV(){

  if(motion == false){return 0;}
  
  long distance = get_distance();
  int velocity = 0;
  
  if(distance - target > 3){
      velocity = v1 + 3 * abs(distance - target);
    }else if(distance < target + 3 && distance > target - 3){
      velocity = v1;
    }else if(distance < target - 3){
       velocity = v1 - 3 * abs(distance - target);
    }

  if(velocity < minSpeed){
    velocity = 0;
    }

  if(velocity > maxSpeed){
      velocity = maxSpeed;
    }

    return velocity;
}
