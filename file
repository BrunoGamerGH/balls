package me.bruno.whatthefuck;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class Phil extends ServerPlayer {
    private ServerPlayer phil;
    private double damage = 0.00;
    private boolean canHit = true;
    protected PathNavigation navigation;
    private final GoalSelector goalSelector;


    public Phil(MinecraftServer minecraftserver, ServerLevel worldServer, GameProfile gameprofile) {
        super(minecraftserver, worldServer, gameprofile);
        this.goalSelector = new GoalSelector(worldServer.getProfilerSupplier());
        navigation = createNavigation(worldServer);

    }

    public PathNavigation getNavigation() {
        return this.navigation;
    }

    public double getDamage() {
        return damage;
    }
    public void setDamage(float damage) {
      this.damage = damage;
    }

    protected PathNavigation createNavigation(Level world) {
        return new GroundPathNavigation(getDummyInsentient(world),world);
    }

    public ServerPlayer getHandle() {
        return this;
    }
    private Mob getDummyInsentient(Level world) {
        return new Mob(EntityType.VILLAGER, world) {
        };
    }



    public void damagePhil(Player player) {
        if (canHit) {
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundAnimatePacket(phil, ClientboundAnimatePacket.HURT));
            player.getWorld().playSound(phil.getBukkitEntity().getLocation(), Sound.ENTITY_PLAYER_HURT,0.25f,1);
        }



            double initDamage = 1.00;
            if (player.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_SWORD)) {
                initDamage = 8.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_SWORD)) {
                initDamage = 7.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD)) {
                initDamage = 6.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.STONE_SWORD)) {
                initDamage = 5.00;
            }else if (player.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_SWORD)) {
                initDamage = 4.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_SWORD)) {
                initDamage = 4.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_AXE)) {
                initDamage = 10.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_AXE)) {
                initDamage = 9.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.IRON_AXE)) {
                initDamage = 9.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.STONE_AXE)) {
                initDamage = 9.00;
            }else if (player.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_AXE)) {
                initDamage = 7.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_AXE)) {
                initDamage = 7.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_SHOVEL)) {
                initDamage = 6.50;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_SHOVEL)) {
                initDamage = 5.50;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.IRON_SHOVEL)) {
                initDamage = 4.50;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.STONE_SHOVEL)) {
                initDamage = 3.50;
            }else if (player.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_SHOVEL)) {
                initDamage = 2.50;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_SHOVEL)) {
                initDamage = 2.50;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_PICKAXE)) {
                initDamage = 6.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_PICKAXE)) {
                initDamage = 5.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.IRON_PICKAXE)) {
                initDamage = 4.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.STONE_PICKAXE)) {
                initDamage = 3.00;
            }else if (player.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_PICKAXE)) {
                initDamage = 2.00;
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_PICKAXE)) {
                initDamage = 2.00;
            }






            Location loc = phil.getBukkitEntity().getLocation();




            if (player.getAttackCooldown() >= 0.9) {
                if (player.getVelocity().getY() < 0 && !player.isOnGround() && player.getLocation().getBlock().getType() != Material.WATER) {
                    if (canHit)
                        damage = Math.round(((initDamage*player.getAttackCooldown()) * 1.5) * 10.0) / 10.0;

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT,0.25f,1);
                    player.getWorld().spawnParticle(Particle.CRIT, loc.getX(), loc.getY() + 1, loc.getZ(),0);
                } else if (player.isSprinting()) {
                    if (canHit)
                        damage = Math.round((initDamage*player.getAttackCooldown()) * 10.0) / 10.0;

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK,0.25f,1);

                } else if (player.getInventory().getItemInMainHand().getType().toString().contains("SWORD")) {
                    if (canHit)
                        damage = Math.round((initDamage*player.getAttackCooldown()) * 10.0) / 10.0;

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP,0.25f,1);
                } else {
                    if (canHit)
                        damage = Math.round((initDamage*player.getAttackCooldown()) * 10.0) / 10.0;

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG,0.25f,1);
                }
            } else {
                if (canHit)
                    damage = Math.round((initDamage*player.getAttackCooldown()) * 10.0) / 10.0;

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_WEAK,0.25f,1);

            }

        if (canHit) {
            for (int i = 0; i<(int) Math.round((damage/2.00001) * 10) / 10; i++) {
                player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR,loc.getX(),loc.getY() + 1, loc.getZ(),0,(Math.random() * (3 - (-3) + 1) + (-3)) / 10,(Math.random() * (1 - (-1) + 1) + (-1)) / 10,(Math.random() * (3 - (-3) + 1) + (-3)) / 10);
            }
            canHit = false;
            new BukkitRunnable() {

                @Override
                public void run() {
                    canHit = true;
                    cancel();
                }
            }.runTaskTimer(Whatthefuck.getPlugin(),10,0);
        }





    }


    public void spawnPhil(Player p) {

        CraftPlayer player = (CraftPlayer) p;




        ServerPlayer npc = getBukkitEntity().getHandle();



        npc.setPos(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());




        ServerGamePacketListenerImpl ps = player.getHandle().connection;
        ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
        ps.send(new ClientboundAddPlayerPacket(npc));
        ps.send(new ClientboundSetEntityDataPacket(npc.getId(),npc.getEntityData(), true));

        p.sendMessage("poggers");
        this.phil = npc;
        phil.setNoGravity(false);

    }

    public int getID() {
        return (phil == null) ? 0 : phil.getId();
    }

}
