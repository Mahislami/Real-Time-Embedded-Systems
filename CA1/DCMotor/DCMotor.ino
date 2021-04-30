const int pwmOutput1 = 11;
const int pwmOutput2 = 10;
const int power = 2;
const int speedUp = 3;
const int speedDown = 4;
const int rotation = 5;
int motorSpeed = 255;
int motorDir = 0; //->
bool on = true;

  
void setup() {
  // put your setup code here, to run once:
  pinMode(pwmOutput1, OUTPUT);
  pinMode(pwmOutput2, OUTPUT);
  pinMode(power, INPUT);
  pinMode(speedUp, INPUT);
  pinMode(speedDown, INPUT);
  pinMode(rotation, INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(on){
    if(motorDir == 0){
      analogWrite(pwmOutput1, motorSpeed);
      analogWrite(pwmOutput2, 0);
    }
    else{
      analogWrite(pwmOutput2, motorSpeed);
      analogWrite(pwmOutput1, 0);
      }
  }
  else {
      analogWrite(pwmOutput1, 0);
      analogWrite(pwmOutput2, 0);
    }
  
  if (digitalRead(power) == HIGH){
    delay(500);
    on = !on;
  }

  if( digitalRead(speedUp) == HIGH) {
    delay(500);
    if(motorSpeed + 10 <= 255) {
      motorSpeed = motorSpeed + 10;
    } else {
        motorSpeed = 255;
      }
    }
  if( digitalRead(speedDown) == HIGH) {
    delay(500);
    if(motorSpeed - 10 >= 0) {
      motorSpeed = motorSpeed - 10;
    } else {
        motorSpeed = 0;
      }
    }
  if(digitalRead(rotation) == HIGH){
    delay(500);
    if (motorDir == 0){
        motorDir = 1; //<-
    }
    else{
      motorDir = 0;
      } 
    }
}
