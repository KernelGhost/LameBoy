# LameBoy

<img src="./Images/1.png" alt="The LameBoy Game Console" style="display: block; margin-left: auto; margin-right: auto; height: 600px;">

<center><i>A DIY handheld game console that's so bad, it's good.</i></center>

## Overview
This project aims to create a retro-style handheld game console using an Arduino Mega 2560.

The console runs a CHIP-8 emulator and comes pre-loaded with 8 games.
1. Blinky (Pac-Man)
2. Tetris
3. Brick
4. Snake
5. Space Invaders
6. Hidden
7. Pong (Single Player)
8. Kaleidoscope

## Images
<table>
  <tr>
    <td><img src="./Images/2.png" width="150px"></td>
    <td><img src="./Images/3.png" width="150px"></td>
    <td><img src="./Images/4.png" width="150px"></td>
    <td><img src="./Images/5.png" width="150px"></td>
    <td><img src="./Images/6.png" width="150px"></td>
  </tr>
</table>

## Hardware Components
- Arduino Mega 2560
- LCD Display
- Buttons (Select, Up, Down, Left & Right)
- Buzzer
- Power Switch
- Li-ion Battery
- USB-Powered Li-ion Battery Charger with Variable DC Output PCB

> [!IMPORTANT]  
> To ensure stability as well as prevent overheating, it is recommended to power the Arduino Mega with an input DC voltage between 7-12 volts.

### Getting Started
1. Clone the repository.

   ```bash
   git clone https://github.com/KernelGhost/LameBoy
   ```

2. Upload the code.
   - Connect your Arduino Mega 2560 to your computer.
   - Open the Arduino IDE and select the correct board and port.
   - Upload the `LAMEBOY.ino` file to the Arduino.

3. Connect the Arduino Mega 2560 to the LCD display, buttons, battery, and other components.

## Customisation
### Game ROMs
Since the LameBoy runs a CHIP-8 emulator, many existing CHIP-8 programs can be easily ported to the platform. However, due to the limited number of buttons on LameBoy compared to the standard CHIP-8 (16 buttons on CHIP-8 vs. 5 on LameBoy), some ROMs may need to be modified. This often involves editing the ROM file in a hex editor to remap the controls. Examples of how to do this are included in this repository.

### Button Layout
You can modify the Arduino code to implement a larger number of buttons. This is simply a matter of declaring additional pins and modifying the button input code to correctly communicate these additional buttons to the emulator.

## Contributing
Contributions are welcome! Feel free to submit pull requests or issues.

## License
This project is licensed under the [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).
