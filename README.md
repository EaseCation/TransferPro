# TransferPro
Transfer plugin based on origin transfer for Nukkit

该插件基于 Minecraft Bedrock 的原生 Transfer 实现群组服务器的人数同步与便捷跨服

前置插件：[DbLib](https://github.com/fromgate/DbLib/releases)

## 具体功能

1. 对多服务器的实时状态的统一管理与查看
2. 通过「群组」与「服务器ID」跨服
    1. 只输入「群组」时，将随机进入群组中的一个服务器
    2. 只输入「服务器ID」时，可跨群组匹配到该服务器ID
3. 服务器人数同步，可显示为 _所有服务器总人数_ 或 _某个群组的总人数_
4. 提供API
    - 可获取所有服务器的实时信息
    - 可控制和自定义玩家跨服时的行为与提示

## 基础配置步骤

1. [配置DbLib插件](https://github.com/fromgate/DbLib#configuration)的数据源，使群组服内所有服务器都连接到同一个数据源
2. 开启群组服内所有Nukkit，你需要为每个Nukkit配置其「群组（可选）」、「服务器ID」、「可直连地址（不包含端口）」
    - 在控制台输入指令：`tspro setme [群组] <服务器ID> <可直连地址>`  
    如 `/tspro setme group1 server1 play.easecation.net`
3. 当在所有Nukkit中配置完成后，在控制台输入指令`tspro dump`，如果配置正确，你将看到所有服务器的实时信息

## 指令

#### 控制台相关指令 `tspro`

- 默认权限：仅控制台
- 使用方法：`tspro <setme|dump|exportlang>`
  - `tspro setme [群组] <服务器ID> <可直连地址>` 配置本服务器的基础信息（详见[基础配置步骤](#基础配置步骤)）
  - `tspro dump [群组]` 列出所有服务器信息
  - `tspro exportlang` 导出语言文件至插件目录下的lang.ini，可自定义修改配置，重启生效

#### 玩家自身跨服指令 `/transfer`

- 指令别名：`/ts`
- 默认权限：所有玩家
- 使用方法：`/transfer [群组] <服务器ID>`

#### 使某玩家跨服 `/transferplayer`

- 指令别名：`/tsp`
- 默认权限：仅OP
- 使用方法：`/transferplayer <目标玩家> [群组] <服务器ID>`