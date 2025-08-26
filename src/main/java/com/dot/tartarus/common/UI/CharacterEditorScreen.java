package com.dot.tartarus.common.UI;

import com.dot.tartarus.Network.Packets.GenderPacket;
import com.dot.tartarus.Network.Packets.SkinPacket;
import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import com.dot.tartarus.common.Utils.PacketSyncUtils;
import com.dot.tartarus.common.Utils.SkinUtil;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dot.tartarus.common.Utils.PacketSyncUtils.sendAllCapabilitiesTo;
import static com.dot.tartarus.common.Utils.PacketSyncUtils.sendCapabilitiesToAll;
import static com.mojang.blaze3d.platform.Lighting.*;
import static net.minecraft.client.gui.components.Button.builder;


public class CharacterEditorScreen extends Screen {

    private final Player playerModel1;

    // Represents one configurable attribute (metadata only: name + min/max)
    private static class Attribute {
        String name;
        int min;
        int max;

        Attribute(String name, int min, int max) {
            this.name = name;
            this.min = min;
            this.max = max;
        }
    }

    // Attributes list
    private final List<Attribute> attributes = new ArrayList<>();

    public CharacterEditorScreen() {
        super(Component.literal("editor"));

        Minecraft mc = Minecraft.getInstance();
        this.playerModel1 = mc.player;

        // Define all attributes here with min/max
        attributes.add(new Attribute("Gender", 0, 2));     // 0 = Male, 1 = Female, 2 = Other
        attributes.add(new Attribute("Skin", 0, 4));       // 0–5 skins
        attributes.add(new Attribute("Hair", 0, 10));      // 0–10 hairs
        attributes.add(new Attribute("Eyes", 0, 7));       // 0–7 eye shapes
        attributes.add(new Attribute("Hair Color", 0, 15));// 0–15 colors
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        // Titles
        guiGraphics.drawCenteredString(this.font, "Preview 1", this.width / 4, 30, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "Preview 2", this.width * 3 / 4, 30, 0xFFFFFF);

        int relX = this.width / 2;
        int relY = this.height / 2;

        // Render player
        int scale = 40;
        net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics, relX, relY, scale,
                (float) (relX + 51) - mouseX,
                (float) (relY + 75 - 50) - mouseY,
                this.playerModel1
        );

        // Draw attribute labels and current values (from capabilities)
        int startY = 60;
        int spacing = 40;
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = attributes.get(i);
            int y = startY + i * spacing;

            int currentValue = getCurrentValueFromCapability(attr.name);

            guiGraphics.drawCenteredString(this.font,
                    attr.name + ": " + currentValue,
                    this.width / 2,
                    y,
                    0xFFFFFF
            );
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        int buttonWidth = 20;
        int buttonHeight = 20;
        int startY = 50;
        int spacing = 40;

        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = attributes.get(i);
            final Attribute currentAttr = attr; // stable reference per iteration
            int y = startY + i * spacing;

            // "-" button
            this.addRenderableWidget(Button.builder(Component.literal("-"), btn -> {
                int value = getCurrentValueFromCapability(currentAttr.name);
                if (value > currentAttr.min) {
                    value--;
                    sendAttributeUpdatePacket(currentAttr.name, value);
                }
            }).bounds(this.width / 2 - 40, y, buttonWidth, buttonHeight).build());

            // "+" button
            this.addRenderableWidget(Button.builder(Component.literal("+"), btn -> {
                int value = getCurrentValueFromCapability(currentAttr.name);
                if (value < currentAttr.max) {
                    value++;
                    sendAttributeUpdatePacket(currentAttr.name, value);
                }
            }).bounds(this.width / 2 + 20, y, buttonWidth, buttonHeight).build());
        }

        // Finish button
        int finishButtonWidth = 100;
        int finishButtonHeight = 20;
        this.addRenderableWidget(Button.builder(Component.literal("Finish"), btn -> this.onClose())
                .bounds(this.width / 2 - finishButtonWidth / 2, this.height - finishButtonHeight - 10, finishButtonWidth, finishButtonHeight)
                .build());
    }

    // Get current value directly from capability
    private int getCurrentValueFromCapability(String key) {
        switch (key) {
            case "Gender":
                return playerModel1.getCapability(IGenderProvider.Gender)
                        .map(cap -> cap.getGender())
                        .orElse(0);

            case "Skin":
                return playerModel1.getCapability(ISkinProvider.Skin)
                        .map(cap -> cap.getSkin())
                        .orElse(0);


            default:
                return 0;
        }
    }

    // Send packet to server
    private void sendAttributeUpdatePacket(String key, int value) {
        switch (key) {
            case "Gender":
                TRNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new GenderPacket(value));
                break;
            case "Skin":
                TRNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new SkinPacket(value));
                SkinUtil.ClearCache(playerModel1);
                break;

        }

    }
}