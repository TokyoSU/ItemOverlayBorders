# ItemOverlayBorder

**ItemOverlayBorder** is a Forge mod for **Minecraft 1.20.1** that adds animated, rarity-colored borders around item icons.

Any item whose rarity is above Common receives a border based on its rarity color, making uncommon, rare, epic, legendary, custom-rarity, and other special items much easier to identify at a glance.

The effect is integrated into Minecraft's normal item rendering as well as several popular item-list and UI mods.

## Features

- Animated rarity-colored borders around non-Common items.
- Uses the item's actual rarity/style color.
- Works in normal Minecraft container screens.
- Works on hotbar item icons.
- JEI integration.
- REI integration.
- EMI integration.
- CashShop integration.
- Compatible with custom rarity setups that expose their rarity color through the item's rarity style.
- Client configuration option to disable the animation and keep the border static.
- No manual rarity/color list is required: the border is derived from the item itself.

## Supported Interfaces

ItemOverlayBorder currently injects its border renderer into:

| Interface | Support |
| --- | --- |
| Minecraft inventories / containers | Yes |
| Minecraft hotbar | Yes |
| JEI | Yes |
| REI | Yes |
| EMI | Yes |
| CashShop | Yes |

The integrations are designed so the same rarity border is shown consistently wherever the item is displayed.

## Rarity Colors

The border color is read from the item's rarity style.

Common items are intentionally ignored, keeping normal/common inventories clean while visually highlighting more important items.

This also makes ItemOverlayBorder a useful companion for custom rarity systems such as **RarityJS**, because custom item rarity colors can be reflected directly by the overlay when provided through the item's rarity style.

## Configuration

The client configuration file is generated at:

```text
config/itemoverlayborder.toml
```

Current option:

```toml
["Item Overlay Border Config"]
    # Remove animation from the item overlay, leaving a static colored border.
    "Disable animation" = false
```

Set `Disable animation` to `true` if you prefer a stationary rarity border.

## Requirements

- Minecraft **1.20.1**
- Minecraft Forge **47+**
- Java **17**
- ApocalypseLib **1.1.5+**

## Optional Integrations

ItemOverlayBorder contains compatibility for the following mods when they are present:

- JEI
- REI
- EMI
- CashShop

Custom rarity mods can also benefit from ItemOverlayBorder as long as their item rarity/style exposes the intended color.

## Installation

1. Install Minecraft Forge 47.x for Minecraft 1.20.1.
2. Install ApocalypseLib 1.1.5 or newer.
3. Place the ItemOverlayBorder `.jar` in your `mods` folder.
4. Optionally install JEI, REI, EMI, CashShop, or your preferred rarity mod.
5. Start Minecraft.

No additional setup is required for the default animated border effect.

## Building From Source

ItemOverlayBorder uses Gradle and ForgeGradle.

Clone/download the project and run:

### Windows

```bat
gradlew.bat build
```

### Linux / macOS

```bash
./gradlew build
```

The compiled mod will be generated under:

```text
build/libs/
```

## Technical Notes

- Minecraft version: `1.20.1`
- Forge version used by the project: `47.4.16`
- Java version: `17`
- Mod ID: `itemoverlayborder`
- Current source version: `1.0.5`
- License: Apache License 2.0

The renderer uses a precomputed 16×16 item-slot perimeter and animates two fading highlights around that perimeter using the rarity RGB color.

## License

ItemOverlayBorder is licensed under the **Apache License 2.0**.
