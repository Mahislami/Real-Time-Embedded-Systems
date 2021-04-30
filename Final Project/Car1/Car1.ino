const int motorLF = 6;
const int motorLB = 11;
const int motorRF = 9;
const int motorRB = 10;

const int rightIR = 2;
const int leftIR = 3;
int leftSensor = 0;
int rightSensor = 0;

const int minSpeed = 40;
const int maxSpeed = 150;

int t = 62;
int v = 55;
int a = 10;
int rotate = 6;
int v_rotate_dif = 40;

void setup() {
pinMode(2,INPUT);
pinMode(3,INPUT);
pinMode(6,OUTPUT);   //left motors forward
pinMode(10,OUTPUT);   //left motors reverse
pinMode(11,OUTPUT);   //right motors forward
pinMode(9,OUTPUT);   //right motors reverse
Serial.begin(9600);
 
}
 
void loop() {

  drive(v);

  while(1){

    drive(v);
    
    leftSensor = digitalRead(leftIR);
    rightSensor = digitalRead(rightIR);
    
    if(digitalRead(leftIR) == 1 && digitalRead(rightIR) == 1){
      drive(v);
    }else if(digitalRead(leftIR) == 0 && digitalRead(rightIR) == 0){
      drive(v);
    }else if(digitalRead(leftIR) == 1 && digitalRead(rightIR) == 0){
      analogWrite(motorLF, 0);//max(v - v_rotate_dif, minSpeed));
      analogWrite(motorLB, 0);
      analogWrite(motorRF, v);
      analogWrite(motorRB, 0);
      while(digitalRead(leftIR) == 1 && digitalRead(rightIR) == 0){
      }
      drive(v);
    }else if(digitalRead(rightIR) == 1 && digitalRead(leftIR) == 0){
      analogWrite(motorLF, v);
      analogWrite(motorLB, 0);
      analogWrite(motorRF, 0);//max(v - v_rotate_dif, minSpeed));
      analogWrite(motorRB, 0);
      while(digitalRead(rightIR) == 1 && digitalRead(leftIR) == 0){
      }
      drive(v);
    }
    
  /*delay(100);*/
  }

}

 void drive(int speed) {
   analogWrite(motorLF, speed + rotate);
   digitalWrite(motorLB, LOW); 
   analogWrite(motorRF, speed - rotate);
   digitalWrite(motorRB, LOW);
  }
