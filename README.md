<img style="text-align:center" src="img/banner.png">

![Mod Loader](https://img.shields.io/badge/mod%20loader-fabric%2c%20forge-a64581?style=flat)
![Enviroment](https://img.shields.io/badge/environment-client%2c%20opt%20server-536a9e?style=flat)
[![Discord](https://img.shields.io/discord/1016206797389975612?style=flat&color=blue&logo=discord&label=Discord)](https://discord.gg/WcYsDDQtyR)

[![Modrinth](https://img.shields.io/badge/dynamic/json?style=flat&color=158000&label=downloads&prefix=+%20&query=downloads&url=https://api.modrinth.com/api/v1/mod/6FtRfnLg&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)](https://modrinth.com/mod/do-a-barrel-roll)
[![CurseForge](https://cf.way2muchnoise.eu/full_663658_downloads.svg)](https://curseforge.com/minecraft/mc-mods/do-a-barrel-roll)

Do a Barrel Roll is a lightweight, fully clientside mod for Fabric and Forge that changes 
elytra flight to be more fun and semi-realistic.
It achieves this by redesigning movement with the 
now completely unlocked camera orientation in mind, 
allowing for full pitch, yaw and roll control in flight.

Some subtle and less subtle modifiers are also applied to the camera,
including but not limited to smoothing and banking.

![](img/ravine.gif)

## Controls

The default controls are as follows, but can be modified:

- Mouse x axis to roll
- Mouse y axis to pitch
- strafe keys (normally A and D) to yaw

## Configuration

The mod can be configured in-game using ModMenu and a compatible config library.
Currently, both Cloth Config and YACL are supported.
Once you install either one, 
you can access the config screen by finding the mod in ModMenu and pressing the config button.

A wide range of options are available, including custom mouse behavior, elytra activation restrictions and
changing values of modifiers like banking, sensitivity and more.

![](img/do-a-barrel-roll.gif)

## Disclaimer

This mod does not actually modify the flight physics themselves, 
so in most cases it shouldn't trigger anticheat. 
However, doing things like loop de loops looks to the server like rapid camera movement
and may get you flagged.
**Use at your own risk.**

Big thanks to [MehVahdJukaar](https://github.com/MehVahdJukaar) for their help with the Forge port.

## For server owners

The mod includes a "thrusting" feature that is probably considered cheating by most servers, 
as it lets users accelerate without using fireworks.
If you want to completely ban players from using this feature on your server, you can either:

- Install the mod on your server and set `"allowThrusting"` to `false` in `config/do_a_barrel_roll-server.json`
- Send a custom packet to the client at login on the `do_a_barrel_roll:config_sync` channel with one 
string field containing the following JSON:
```json
{
  "allowThrusting": false
}
```
The client will respond with a boolean on the same channel to indicate success.

This option will be respected by any clients using version 2.4.0 or later of the mod. 
The Forge version does not currently support thrusting, so it will not be affected by this option.