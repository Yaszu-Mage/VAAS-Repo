package org.firstinspires.ftc.teamcode.yahoo_api;

import com.qualcomm.robotcore.hardware.Gamepad;

public class gamepad {
    static Gamepad gamepad_instance;

    public gamepad(Gamepad gamepad) {
        gamepad_instance = gamepad;
    }

    public enum TriggerMode {
        DeadMan,
        //Will stay active until let go, even if pressed back + delay to reset
        Boolean,
        // Where Trigger is true or false if it is being pressed
        Range,
        // Where trigger goes from 1 to zero like normal gamepad
        Lever,
        //Minecraft Lever
    }
    public boolean a = gamepad_instance.a;
    public boolean b = gamepad_instance.b;
    public boolean x = gamepad_instance.x;
    public boolean y = gamepad_instance.y;
    public boolean left_bumper = gamepad_instance.left_bumper;
    public boolean right_bumper = gamepad_instance.right_bumper;
    public boolean left_stick_button = gamepad_instance.left_stick_button;
    public boolean right_stick_button = gamepad_instance.right_stick_button;
    public Vector2 right_stick = new Vector2(gamepad_instance.right_stick_x,gamepad_instance.right_stick_y);
    public Vector2 left_stick = new Vector2(gamepad_instance.left_stick_x,gamepad_instance.left_stick_y);
    public Object right_trigger = right_trigger_func();
    public boolean start = gamepad_instance.start;
    public TriggerMode LTMode = TriggerMode.Range;
    public TriggerMode RTMode = TriggerMode.Range;
    static Float deadman_old_value = null;
    Boolean lever_value = null;
    public Vector2 left_stick() {
        return new Vector2(gamepad_instance.left_stick_x,gamepad_instance.left_stick_y);
    }
    public Vector2 right_stick() {
        return new Vector2(gamepad_instance.right_stick_x,gamepad_instance.right_stick_y);
    }
    public Object right_trigger_func() {
        switch (RTMode){
            case Range:
                return gamepad_instance.right_trigger;
            case Boolean:
                if (gamepad_instance.right_trigger > 0.1) {
                    return true;
                } else {
                    return false;
                }
            case DeadMan:
                if (gamepad_instance.right_trigger != 1) {return false;} else {return true;}
            case Lever:
                //TODO finish this function
                return false;
        }
        return false;
    }



}
