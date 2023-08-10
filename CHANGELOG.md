- Made the server-side config configurable from the client.
    - Only operators with at least permission level 3 or the `do_a_barrel_roll.configure` permission can change the config.
    - The updated config will be automatically synced to all connected clients.
- Added a server-side config option to change kinetic damage behaviour.
- Fixed a bug causing handshake state to reset when a player dies.

- The .1 release fixes a critical issue with an incomplete mixin refmap causing a crash on startup.