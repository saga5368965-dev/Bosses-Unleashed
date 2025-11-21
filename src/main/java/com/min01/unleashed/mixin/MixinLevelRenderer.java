package com.min01.unleashed.mixin;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.unleashed.entity.IShaderEffect;
import com.min01.unleashed.shader.UnleashedShaderEffects;
import com.min01.unleashed.util.UnleashedClientUtil;
import com.min01.unleashed.util.UnleashedUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

@Mixin(value = LevelRenderer.class, priority = -10000)
public class MixinLevelRenderer
{
	@Inject(at = @At(value = "TAIL"), method = "renderLevel")
	private void renderLevel(PoseStack mtx, float frameTime, long nanoTime, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture light, Matrix4f projMat, CallbackInfo ci)
	{
		Vec3 camPos = camera.getPosition();
		RenderSystem.depthMask(false);
		new ArrayList<>(UnleashedShaderEffects.EFFECTS).forEach(t -> 
		{
			if(UnleashedClientUtil.MC.level.dimension() == t.dimension)
			{
				Vec3 worldPos = t.pos;
				Vec3 pos = worldPos.subtract(camPos);
				mtx.pushPose();
				mtx.translate(pos.x, pos.y, pos.z);
				if(t.name.equals("Shockwave"))
				{
	    			UnleashedClientUtil.applyShockwave(mtx, frameTime, t.tickCount);
				}
				mtx.popPose();
			}
		});
		for(Entity entity : UnleashedUtil.getAllEntities(UnleashedClientUtil.MC.level))
		{
			if(!(entity instanceof IShaderEffect effect) || !effect.shouldApplyEffect())
			{
				continue;
			}
			mtx.pushPose();
			if(effect.getEffetName().equals("Galaxy"))
			{
				Vec3 pos = effect.getEffectPosition(entity).subtract(camPos);
				float scale = effect.getEffectScale() * 0.1F;
				mtx.translate(pos.x, pos.y + effect.getOffset(), pos.z);
				Vector3f vector3f = camera.getLookVector();
				if(effect.cameraRotation())
				{
					mtx.mulPose(camera.rotation());
					mtx.mulPose(Axis.XP.rotationDegrees(90.0F));
					vector3f = camera.getUpVector();
				}
				UnleashedClientUtil.applyGalaxy(mtx, frameTime, effect.getEffectTickCount(), scale, vector3f);
			}
			else if(effect.getEffetName().equals("Wormhole"))
			{
				Vec3 pos = effect.getEffectPosition(entity).subtract(camPos);
				mtx.translate(pos.x, pos.y + effect.getOffset(), pos.z);
				UnleashedClientUtil.applyWormhole(mtx, frameTime);
			}
			else if(effect.getEffetName().equals("Blackhole"))
			{
				Vec3 pos = effect.getEffectPosition(entity).subtract(camPos);
				mtx.translate(pos.x, pos.y, pos.z);
				UnleashedClientUtil.applyBlackhole(mtx, frameTime, effect.getEffectTickCount());
			}
			mtx.popPose();
		}
		RenderSystem.depthMask(true);
	}
}
