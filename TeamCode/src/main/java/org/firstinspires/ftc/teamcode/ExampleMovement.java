package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Robot: Teleop POV", group="Robot") // Makes this file show up on the Driver station
public class ExampleMovement extends LinearOpMode {

    // Declare drivetrain motor variables
    public DcMotorEx leftBackDrive      = null;
    public DcMotorEx rightBackDrive     = null;
    public DcMotorEx leftFrontDrive     = null;

    public DcMotorEx rightFrontDrive    = null;

    public Servo    leftClaw            = null;


    @Override
    public void runOpMode() {


        // Define and Initialize Motors
        leftBackDrive  = hardwareMap.get(DcMotorEx.class, "leftBack");
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "rightBack");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "rightFront");

        // Define and Initialize Servos
        leftClaw  = hardwareMap.get(Servo.class, "leftClawServo");

        // If left side motors were mounted backwards ...
        // Allows similar behavior of motors to inputs regardless of motor orientation
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        // Achieve smooth deceleration -> zero power behavior = float
        // Achieve harsh deceleration -> zero power behavior = brake
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


        // Set home position of servo:
        // Range: [0, 1]
        leftClaw.setPosition(0);

        // Send telemetry message to signify robot waiting;
        telemetry.addData(">", "Robot Ready.  Press Play.");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (isStopRequested()) return;

        Gamepad previousGamepad1 = new Gamepad();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            gamepad1.copy(previousGamepad1);

            // Get user joystick input & multiply by -1 to make up direction positive
            double movementInput = -gamepad1.left_stick_y;

            // Set Drivetrain motor power
            // Range: [-1, 1]
            leftFrontDrive.setPower(movementInput);
            leftBackDrive.setPower(movementInput);
            rightFrontDrive.setPower(movementInput);
            rightBackDrive.setPower(movementInput);

            // Ensure that input is new to this current frame
            // Called "rising edge detector"
            if (gamepad1.a && !previousGamepad1.a) {
                leftClaw.setPosition(1);
            } else if (!gamepad1.a) {
                leftClaw.setPosition(0);
            }

            telemetry.update();
        }
    }
}
