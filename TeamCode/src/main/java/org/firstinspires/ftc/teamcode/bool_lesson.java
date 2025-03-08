package org.firstinspires.ftc.teamcode;

public class bool_lesson {
    boolean jun = false;
    boolean eflerb = false;
    int derek = 3;

    public void awesome() {
        derek += 1;
        if (derek == 4) {
            System.out.println("This is awesome");
        }
        if (eflerb || jun) {
            System.out.println("This is awesome");
        }
        if (eflerb && jun) {
            System.out.println("Balls");
        }
        if (!eflerb || !jun) {

        }
    }
}
