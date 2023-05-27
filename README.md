<img style="text-align:center" src="img/banner.png">

![Mod Loader](https://img.shields.io/badge/mod%20loader-fabric%2c%20forge-a64581?style=flat)
![Enviroment](https://img.shields.io/badge/environment-client%2c%20opt%20server-536a9e?style=flat)
[![Discord](https://img.shields.io/discord/1016206797389975612?style=flat&color=blue&logo=discord&label=Discord)](https://discord.gg/WcYsDDQtyR)

[![Modrinth](https://img.shields.io/modrinth/dt/do-a-barrel-roll)](https://modrinth.com/mod/do-a-barrel-roll)
[![CurseForge](https://cf.way2muchnoise.eu/full_663658_downloads.svg)](https://curseforge.com/minecraft/mc-mods/do-a-barrel-roll)

Do a Barrel Roll is a lightweight, fully clientside mod for Fabric and Forge that changes 
elytra flight to be more fun and semi-realistic.
It achieves this by redesigning movement with a 
completely unlocked camera orientation in mind, 
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

The mod can be configured in-game using ModMenu on Fabric, or the builtin mods button on Forge and a compatible config library.
Currently, Cloth Config, YACL and Configured are supported.
Once you install any of these, 
you can access the config screen by finding the mod in your mod menu of choice and pressing the config button.

A wide range of options are available, including custom mouse behavior, elytra activation restrictions and
changing values of modifiers like banking, sensitivity and more.

## Server-side features

Visual aspects of the mod (playermodel roll in particular) can be synced between 
clients by installing it on the server-side as well. 
Everything is still fully compatible with both vanilla clients and servers. 
All the following configurations are valid:

- Server with mod, client with mod: visuals are synced
- Server with mod, client without mod: client can join and play without issues, but can't see visuals
- Server without mod, client with mod: client can join and play without issues, but can only see their own visuals
- Server with mod, client 1 with mod, client 2 with mod, client 3 without mod: client 1 and 2 can see each other's visuals, client 3 cannot
- Server without mod, client 1 with mod, client 2 with mod, client 3 without mod: client 1 and 2 can only see their own visuals

![](img/do-a-barrel-roll.gif)

## Disclaimers

This mod does not actually modify the flight physics themselves, 
so in most cases it shouldn't trigger anticheat. 
However, doing things like loop de loops looks to the server like rapid camera movement
and may get you flagged.
**Use at your own risk.**

**Versions prior to 2.8.3 may not be suitable for use on multiplayer servers due to lax server-side restrictions**

## Credits

Based on the [Cool Elytra Roll](https://github.com/Jorbon/cool_elytra) mod by [Jorbon](https://github.com/Jorbon),
specifically it's "realistic mode".

Ported to Forge by [MehVahdJukaar](https://github.com/MehVahdJukaar).

Mod icon by Mizeno.

## For server owners

The mod includes a "thrusting" feature that is probably considered cheating by most servers, 
as it lets users accelerate without using fireworks.
**This is disabled by default on servers as of 2.8.3.**
If you want to allow players to use this feature on your server, you can either:

- Install the mod on your server and set `"allowThrusting"` to `true` in `config/do_a_barrel_roll-server.json`
- Send a custom packet to the client at login on the `do_a_barrel_roll:config_sync` channel with one 
string field containing the following JSON:
```json
{
  "allowThrusting": true
}
```
The client will respond with a boolean on the same channel to indicate success.

This option will be respected by any clients using version 2.4.0 or later of the mod. 
The Forge version does not support thrusting, so it will not be affected by this option.