# MecanumOdometryControl

**MecanumOdometryControl** là bài nộp cho bài kiểm tra FTC, triển khai OpMode `MecanumDriveOpMode` để điều khiển robot mecanum, sử dụng goBILDA Pinpoint Odometry Computer với 2 bánh odometry, hiển thị X, Y, heading và góc cánh tay qua telemetry, và điều khiển cánh tay đến 45°/90° bằng gamepad. Dự án được phát triển bằng Android Studio và FTC SDK 10.1.

**Yêu cầu**:
1. Điều khiển drivetrain mecanum bằng gamepad (left stick: X/Y, right stick: xoay).
2. Theo dõi X, Y, heading bằng goBILDA Pinpoint với 2 pod odometry.
3. Hiển thị X, Y, heading qua telemetry.
4. Điều khiển cánh tay đến 45° (nút A) và 90° (nút B).

**Giải pháp**:
- **Drivetrain**: `MecanumDrive` xử lý đầu vào gamepad, điều khiển 4 mô-tơ mecanum.
- **Odometry**: `OdometryManager` dùng `GoBildaPinpointDriver` để tính X, Y, heading, tích hợp bộ lọc bổ sung (`alpha = 0.98`) cho heading mượt hơn.
- **Telemetry**: `TelemetryManager` (Singleton) hiển thị X, Y, heading, encoder ticks, trạng thái thiết bị, tần số Pinpoint/vòng lặp. `ArmController` hiển thị góc cánh tay.
- **Cánh tay**: `ArmController` với `PreciseAngleControlStrategy` điều khiển servo đến 45°/90°.
- Code dùng các pattern OOP (Builder, Singleton, Strategy, Dependency Injection, Abstract Factory) để dễ bảo trì.

## Yêu cầu hệ thống

### Phần cứng
- **Drivetrain**: 4 mô-tơ mecanum (goBILDA 5202 Series), tên: `frontLeft`, `frontRight`, `backLeft`, `backRight`. Bánh tạo hình chữ X.
- **Odometry**: goBILDA Pinpoint (`pinpoint`) với 2 pod 4-Bar (X pod: tiến/lùi, Y pod: trái/phải). Offset: `-84.0 mm` (X), `-168.0 mm` (Y).
- **Cánh tay**: 1 servo (goBILDA Dual Mode Servo), tên: `armServo`.
- **Control Hub**: REV Robotics Control Hub.
- **Driver Station**: Điện thoại Android với FTC Driver Station.
- **Gamepad**: Logitech F310/Xbox controller (USB/Bluetooth).
- **Pin**: 12V (>11.5V).

### Phần mềm
- **Android Studio**: 2024.x.
- **FTC SDK**: 10.1, có `GoBildaPinpointDriver`.
- **Git**: Để clone.
- **Java**: JDK 11.

## Hướng dẫn cài đặt

### 1. Clone Repository
```bash
git clone https://github.com/KienPC1234/MecanumOdometryControl.git
cd MecanumOdometryControl
```

### 2. Biên dịch
1. Mở Android Studio, chọn **Open an existing project**, chọn `MecanumOdometryControl`.
2. Nhấn `Sync Project with Gradle Files`.
3. Kiểm tra `build.gradle`:
   ```gradle
   implementation 'org.firstinspires.ftc:robotcore:latest'
   implementation 'org.firstinspires.ftc:hardware:latest'
   ```

### 3. Cấu hình Control Hub
1. Kết nối Control Hub (Wi-Fi: `192.168.43.1` hoặc USB).
2. Truy cập `192.168.43.1:8080`, vào **Configure Robot**.
3. Thêm:
   - Mô-tơ (cổng 0–3): `frontLeft`, `frontRight`, `backLeft`, `backRight`.
   - Servo (cổng 0): `armServo`.
   - I2C Device (cổng I2C 0): `pinpoint`.
4. Lưu cấu hình.

### 4. Chuẩn bị Robot
1. **Drivetrain**: Gắn 4 mô-tơ, kết nối cổng 0–3. Bánh mecanum tạo hình chữ X.
2. **Odometry**: Gắn Pinpoint, kết nối I2C. Gắn pod (X: cổng X, Y: cổng Y). Cập nhật offset nếu khác:
   ```java
   robot.getPinpoint().setOffsets(xOffset, yOffset, DistanceUnit.MM);
   ```
3. **Cánh tay**: Gắn servo (`armServo`), kết nối cổng 0.
4. **Pin**: Sạc đầy (>11.5V).

## Hướng dẫn chạy

### 1. Tải lên Control Hub
1. Kết nối Control Hub (Wi-Fi/USB).
2. Nhấn `Run > Run 'TeamCode'` trong Android Studio.
3. Kiểm tra lỗi:
   - `NoClassDefFoundError`: Xác nhận `GoBildaPinpointDriver`.
   - Kết nối lỗi: Kiểm tra Wi-Fi/USB.

### 2. Chạy OpMode
1. Mở FTC Driver Station, kết nối Control Hub.
2. Chọn `TeleOp` → `MecanumDriveOpMode`.
3. Nhấn `Init`:
   - Telemetry: `Status: Initialized`, `X offset (mm): -84.0`, `Y offset (mm): -168.0`, `Device Version`.
4. Nhấn `Start`.

## Kiểm tra yêu cầu

### 1. Drivetrain
- **Điều khiển**: Left stick (tiến/lùi, trái/phải), right stick X (xoay).
- **Kiểm tra**: Tiến 1m (thẳng), dịch trái 1m (không xoay), xoay 360° (mượt).
- **Sửa lỗi**: Sai hướng → sửa `RobotHardware.java` (`REVERSE`/`FORWARD`). Không chạy → kiểm tra cổng 0–3, pin.

### 2. Odometry
- **Telemetry**: `X (in)` (~39.37 inch/m khi tiến), `Y (in)` (dịch trái), `Heading (deg)` (0°→360°), `Encoder X/Y (ticks)`, `Device Status: READY`.
- **Kiểm tra**: Tiến 1m, dịch trái 1m, xoay 360° → telemetry khớp.
- **Sửa lỗi**: X/Y sai → sửa `EncoderDirection`. Heading lệch → tăng `alpha` (0.99).

### 3. Telemetry
- **Kiểm tra**: Hiển thị `X (in)`, `Y (in)`, `Heading (deg)`, `Encoder X/Y`, `Arm Angle (deg)`, `Device Status`, `Pinpoint Frequency`, `Loop Frequency`.
- **Sửa lỗi**: Không hiển thị → kiểm tra Driver Station, `telemetry.update()`.

### 4. Cánh tay
- **Điều khiển**: Nút A (45°), nút B (90°).
- **Kiểm tra**: A → 45°, B → 90°. Telemetry `Arm Angle (deg)` khớp.
- **Sửa lỗi**: Góc sai → sửa `ANGLE_RANGE` (ví dụ: 170.0) trong `PreciseAngleControlStrategy`. Servo lỗi → kiểm tra cổng 0.
