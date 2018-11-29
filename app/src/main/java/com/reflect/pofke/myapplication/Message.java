package com.reflect.pofke.myapplication;

public class Message {
String theText;
String sender;
String color;
int messageId;
Message(String theText, String sender, String color, int messageId)
{
    this.theText = theText;
    this.sender = sender;
    this.color = color;
    this.messageId = messageId;
}
}
