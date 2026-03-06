package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class Robot extends TimedRobot {
    // Left side - IDs 1 & 2
    private final SparkMax leftLeader = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax leftFollower = new SparkMax(2, MotorType.kBrushless);

    // Right side - IDs 3 & 4
    private final SparkMax rightLeader = new SparkMax(3, MotorType.kBrushless);
    private final SparkMax rightFollower = new SparkMax(4, MotorType.kBrushless);

    private final DifferentialDrive drive = new DifferentialDrive(leftLeader, rightLeader);
    private final XboxController controller = new XboxController(0);

    @Override
    public void robotInit() {
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
    }

    @Override
    public void teleopPeriodic() {
        drive.arcadeDrive(
            -controller.getLeftY(),
            controller.getRightX()
        );
    }
}