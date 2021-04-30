#include <Stepper.h>

const int stepsPerRevolution = 36;  // change this to fit the number of steps per revolution
// for your motor

const int powerButton = 11;
const int cwButton = 12;
const int ccwButton = 13;

int motorDirection  = 1;
boolean on = true;

Stepper myStepper(stepsPerRevolution, 6, 4 , 10, 9);

void setup() {
  pinMode(4, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(powerButton, INPUT);
  pinMode(cwButton, INPUT);
  pinMode(ccwButton, INPUT);
}

void loop() {
  if(on){
    myStepper.step(1 * motorDirection); //rotate 10 degree.
  }

  if(digitalRead(powerButton) == HIGH){
    on = false;
    delay(500);
  }

  if(digitalRead(cwButton) == HIGH){
    if(!on) {on = true;}
    motorDirection = 1;
    delay(500);
  }

  if(digitalRead(ccwButton) == HIGH){
    if(!on) {on = true;}
    motorDirection = -1;
    delay(500);
  }
    
    delay(100);
}
