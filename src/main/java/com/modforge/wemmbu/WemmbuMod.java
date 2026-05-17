package com.modforge.wemmbu;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WemmbuMod implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("wemmbu");

    public static volatile boolean XRAY_ENABLED = false;

    public static KeyBinding OPEN_MENU_KEY;

    @Override
    public void onInitializeClient() {
        try {
            // Use Fabric keybinding API signature: (translationKey, InputUtil.Type, keyCode, categoryTranslationKey)
            OPEN_MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.wemmbu.open_menu",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_KP_MULTIPLY,
                    Text.translatable("category.wemmbu.general")
            ));

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                try {
                    if (client == null || client.player == null) return;
                    while (OPEN_MENU_KEY.wasPressed()) {
                        client.setScreen(new WemmbuModScreen());
                    }
                } catch (Throwable t) {
                    LOGGER.error("Client tick handler failed", t);
                }
            });

            LOGGER.info("Wemmbu Secret Menu XRAY loaded");
        } catch (Throwable t) {
            LOGGER.error("Failed initializing WemmbuMod client", t);
        }
    }
}

class WemmbuModScreen extends Screen {
    protected WemmbuModScreen() {
        super(Text.literal("Secret Menu"));
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("XRAY"), btn -> {
            try {
                WemmbuMod.XRAY_ENABLED = !WemmbuMod.XRAY_ENABLED;
                btn.setMessage(Text.literal(WemmbuMod.XRAY_ENABLED ? "XRAY: ON" : "XRAY"));
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.worldRenderer != null) mc.worldRenderer.reload();
            } catch (Throwable ignored) {
            }
        }).dimensions(8, 8, 60, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawTextWithShadow(this.textRenderer, this.title,
                this.width / 2 - this.textRenderer.getWidth(this.title) / 2, 30, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
