# BossBar Extender — UIStudio compatibility source

Client-side Fabric mod for Minecraft Java. This source is currently configured to build against Minecraft 1.21.11 / Yarn 1.21.11+build.6. The mod metadata allows Minecraft 1.21.1–1.21.11, but this particular JAR is only built against 1.21.11 and is not yet verified as a binary for every intervening version. Separate per-version builds and in-game tests are still required.

## What this revision changes

- It no longer cancels `BossBarHud.render()` or manually redraws BossBars.
- It redirects the scaled-height value used by the vanilla BossBar render cutoff, leaving vanilla's render method, text renderer, active resource-pack font, BossBar titles, and textures in the normal path.
- If the server advertises UIStudio's `uistudio:screen` plugin channel, the client sends the scaled GUI width and a virtual height of three times the actual scaled height.
- UIStudio 2.2.5 accepts a 12-byte big-endian payload containing three integers: its magic value, width, and height. The client payload follows that format. The tripled height offsets UIStudio's internal one-third-screen row calculation.
- UIStudio and RoleplayHUD server JARs are not modified.

## Client requirements

- Fabric Loader for the exact Minecraft version.
- Fabric API for the exact Minecraft version. This revision uses Fabric API 0.141.3+1.21.11 for its 1.21.11 build.
- Install this mod and the matching Fabric API in each player's client `mods` folder. Do not put this client mod in the Paper server `plugins` folder.
- UIStudio remains installed on the Paper server. The screen-size sync is sent only when the server advertises `uistudio:screen`.

## Configuration

File: `.minecraft/config/bossbar-extender.properties`

```properties
max-screen-usage-percent=100
```

The value is clamped between 50 and 100. A previous config containing `85` will keep using 85; change it to `100` to use the full scaled screen height. Restart Minecraft after changing the value.

## Build

Requires JDK 21 and Gradle 9.2.1. Run `gradle build`. The mod JAR is created in `build/libs/`. GitHub Actions packages that JAR into a ZIP artifact.

## Important test notes

This is a source-level compatibility revision, not a confirmed runtime fix yet. The supplied UIStudio 2.2.5 plugin's channel format was inspected, and the mod sends that format, but it still needs a live test with your Paper server, UIStudio resource pack, Fabric client, and each device class. In particular, test at least one PC/laptop and one Android Java launcher.
