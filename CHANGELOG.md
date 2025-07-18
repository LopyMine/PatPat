# v1.2.0 Update, omg!

## Keybinding Updates
- Added way to change standard pat keybinding. This will help if you want to have compatibility with other mods, which uses a `Shift + RMB` combination by default too. It can be just `Left Alt` or `Left Alt + RMB`.

## Networking Updates
- Now players will be able to pat each other on most servers (like CubeCraft, Hoplite and others) **without having a mod/plugin on the server itself**! (and no, we didn't set up a separate server for this,the packets will still go through the server you're playing on, pure magic!)

## Command Updates
- Added `/patpat ratelimit [info | set | enable | disable]` command for preventing spam with pat packets. If this rate limit is enabled, players will have some number of tokens (pats), if a player pats a mob, he spends his tokens. He will receive new tokens after some time. This all can be configured with the subcommands `/patpat ratelimit set [increment | interval | limit]`
- Added better string permissions for commands with Fabric Permissions API. Without it, mod will use standard op level permissions (mod uses `2` level for its commands)
- Added `/patpat info` command which will give information about the current version of the server side mod. Use this command when reporting any bugs!
- Added `/patpat reload` command for reloading server side config. This will help to apply config changes without restarting the server.
- Added `/patpat-client ignore [add | remove]` command which will help to prevent some mobs from being patted.
- Added `/patpat-client info` command which will give information about the current version of the client side mod. Use this command when reporting any bugs!

## Misc
- Updated to 1.21.6–7 (mod still supports all MC versions from 1.16.5–1.21.7!)
- Updated translations and created a special site for easy translating the PatPat mod! You can find it here: https://weblate.nik51.name/project/patpat
- Now if someone is patting you, you will be able to see their pats above your head in the first person.
- The project has been migrated to mojmap mappings (for future compatibility with other loaders).
- Added automatic config migration from an older version.
- Added automatic tests to the project.
- Started migration from old pat packets to new ones. Old packets contain full mob UUID, while new packets contain just int ID. This decision was made for several reasons, including *packet optimization* aka *the packets will now weigh less!*. The PatPat Mod and PatPat Plugin still (temporarily) support old packets, but when possible, new packets will be used automatically by the PatPat Mod and the PatPat Plugin.
- Synced with the plugin for good work (commands, packets, etc.)