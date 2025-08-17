package com.dot.tartarus.common.UI;

import com.dot.tartarus.TartarusMod;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CharacterEditor extends Screen {
    public CharacterEditor(){
        super(Component.literal("editor"));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, "Welcome to the World!", this.width / 2, this.height / 2, 0xFFFFFF);
    }
    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("Continue"), btn -> {
            onClose(); // closes screen
        }).bounds(this.width/2 - 50, this.height/2 + 30, 100, 20).build());
    }
}
