#include <Servo.h>

Servo myservo;  // create servo object to control a servo

int val = 90;    // variable to read the value from the analog pin
const int CLOCK_WISE = 11;
const int UN_CLOCK_WISE = 10;
const int PWM = 9;
int myDegrees[19] = {40 ,50 ,55 ,60 ,66 ,71 ,77 ,82 , 87 , 93 , 98 , 104 ,109 , 114 , 120 , 125 , 130 , 136 , 145};
int i = 9;


void setup() {
  myservo.attach(PWM); // attaches the servo on pin 9 to the servo object
  myservo.write(val);
  pinMode(CLOCK_WISE, INPUT);
  pinMode(UN_CLOCK_WISE, INPUT);
}

void loop() {

     
 if (digitalRead(CLOCK_WISE) == HIGH){
    i++;
    delay(1000);
  }
   
   if (digitalRead(UN_CLOCK_WISE) == HIGH){
    i--;
    delay(1000);
   }

    myservo.write(myDegrees[i]);
}
