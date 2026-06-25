package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;  // Fixed typo: "moter" -> "motor"

public class Robot extends TimedRobot {
    // Left side - IDs 1 & 2
    private final SparkMax leftLeader = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax leftFollower = new SparkMax(2, MotorType.kBrushless);

    // Right side - IDs 3 & 4
    private final SparkMax rightLeader = new SparkMax(3, MotorType.kBrushless);
    private final SparkMax rightFollower = new SparkMax(4, MotorType.kBrushless);

    private final DifferentialDrive drive = new DifferentialDrive(leftLeader, rightLeader);
    private final XboxController controller = new XboxController(0);

    public final double scale_factor = 0.5;
    
    // Intake/shooter motors
    private PWMVictorSPX intakeMotor;
    private PWMVictorSPX shooterMotor;

    @Override
    public void robotInit() {
        // Drive train configuration
        SparkMaxConfig followerConfig = new SparkMaxConfig();
        followerConfig.idleMode(IdleMode.kBrake);

        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightConfig.inverted(true).idleMode(IdleMode.kBrake);

        leftLeader.configure(new SparkMaxConfig().idleMode(IdleMode.kBrake),
            SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);

        leftFollower.configure(followerConfig.follow(leftLeader),
            SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);

        rightLeader.configure(rightConfig,
            SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);

        rightFollower.configure(followerConfig.follow(rightLeader),
            SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);
        
        // Initialize intake/shooter motors
        intakeMotor = new PWMVictorSPX(0);
        shooterMotor = new PWMVictorSPX(1);  // Fixed typo: "shooterMOtor"
    }

    @Override
    public void teleopPeriodic() {  //
        // Drive control
        drive.arcadeDrive(
            (controller.getLeftY() * scale_factor),
            (-controller.getRightX() * scale_factor)
        );

        // Intake mode using button A (everything spins inward)
        if (controller.getAButton()) {
            intakeMotor.set(-0.4);    // Spin in
            shooterMotor.set(0.4);   // Also spin in
        }
        // Shoot mode - motors spin opposite to bring pelota up
        else if (controller.getBButton()) {
            intakeMotor.set(-0.4);    
            shooterMotor.set(-0.4);  
        }
        else {
            intakeMotor.set(0.0);    
            shooterMotor.set(0.0);   
        }
    }
}