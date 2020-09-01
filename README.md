# TransferPro

Transfer plugin based on vanilla transfer for Nukkit

*Requires [DbLib](https://cloudburstmc.org/resources/dblib.12/)*

## Features

1. You can view the real-time status of all servers
2. Transfer in different ways
    1. When only `Group` is provided, the player will be randomly transferred to a server in the group
    2. When only `Server ID` is provided, the player can be transferred to a server in a different group
3. Sync Player count
4. APIs
    - Provides real-time information of all servers
    - Customizable transfer behavior and messages

## Getting Started

1. Makes all servers in a group connect to a same data source. See [DbLib configuration](https://github.com/fromgate/DbLib#configuration)
2. Run all servers in the group. You need to configure `Group (optional)`, `Server ID` and `Public Address` for each server.
    - Enters this command at the console：`tspro setme [Group] <Server ID> <Public Address>`  
    Example: `/tspro setme group1 server1 play.easecation.net`

## Commands

#### Console command `tspro`

- default permission: console
- usage: `tspro <setme|dump|exportlang>`
  - `tspro setme [Group] <Server ID> <Public Address>` Sets up server data (see [Getting Started](#getting_started))
  - `tspro dump [Group]` Displays all server information
  - `tspro exportlang` Exports language files
  - `tspro clean` Cleans up offline server information

#### Transfer to a server `/transfer`

- alias：`/ts`
- default permission：player
- usage: `/transfer [Group] <Server ID>`

#### Transfer other player to a server `/transferplayer`

- alias：`/tsp`
- default permission: op
- usage: `/transferplayer <Player> [Group] <Server ID>`
