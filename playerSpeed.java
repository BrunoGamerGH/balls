
        final int[] ticksWalking = {0};
        final double[] movementMultiplier = {1};
        final double[] module1 = new double[1];
        double module2 = 0.98;
        final double[] effectMultiplier = new double[1];
        AtomicReference<Double> effectModule1 = new AtomicReference<>((double) 1);
        AtomicReference<Double> effectModule2 = new AtomicReference<>((double) 1);
        final double[] slipperinessMultiplier = {0.6};
        final double[] velocity = {0};
                defaultBodyRot = player.yBodyRot;
                if (!(bukkitPlayer.getVelocity().getX() > 0 || bukkitPlayer.getVelocity().getY() > 0 || bukkitPlayer.getVelocity().getZ() > 0)) {
                    updatedBodyRot = defaultBodyRot;
                } else {
                    CraftPlayer craftPlayer = player.getBukkitEntity();
                    LivingEntity playerLiving = craftPlayer.getPlayer();
                    Player spigotPlayer = craftPlayer.getPlayer();

                    if (player.isSprinting()) {
                        // is sprinting
                        module1[0] = 1.3;
                        ticksWalking[0]++;
                    } else if (player.isCrouching()) {
                        // is crouching
                        module1[0] = 0.3;
                        ticksWalking[0]++;
                    } else if (!player.isSprinting()) {
                        // is walking
                        module1[0] = 1.0;
                        ticksWalking[0]++;
                    } else {
                        // isnt moving
                        module1[0] = 0.0;
                        ticksWalking[0] = 0;
                    }
                    movementMultiplier[0] = module1[0] * module2;


                    spigotPlayer.getActivePotionEffects().forEach(potionEffect -> {
                        if (potionEffect.getType().equals(PotionEffectType.SPEED)) {
                            // player has speed
                            effectModule1.set(1 + (0.2 * potionEffect.getAmplifier()));
                        }
                        if (potionEffect.getType().equals(PotionEffectType.SLOW)) {
                            // player has slowness
                            effectModule2.set(1 + (0.2 * potionEffect.getAmplifier()));
                        }
                    });
                    effectMultiplier[0] = (effectModule1.get() * effectModule2.get());


                    Location location = new Location(spigotPlayer.getWorld(),spigotPlayer.getLocation().getBlockX(),spigotPlayer.getLocation().getBlockY(),spigotPlayer.getLocation().getBlockZ());
                    location.setY(location.getBlockY() - 1);
                    if (spigotPlayer.getWorld().getBlockAt(location).getType() == Material.SLIME_BLOCK) {
                        // on slime block

                        slipperinessMultiplier[0] = 0.8;
                    } else if (spigotPlayer.getWorld().getBlockAt(location).getType() == Material.ICE) {
                        // on ice

                        slipperinessMultiplier[0] = 0.98;
                    } else if (spigotPlayer.getWorld().getBlockAt(location).getType() == Material.AIR) {
                        // airborne

                        slipperinessMultiplier[0] = 1;
                    }
                    if (spigotPlayer.getVelocity().getY() == 0) {
                        // ground
                        velocity[0] = (velocity[0] * slipperinessMultiplier[0] * 0.91) + (0.1 * movementMultiplier[0] * effectMultiplier[0] * Math.pow((0.6/slipperinessMultiplier[0]),3)) * Math.sin(player.getSpeed());
                    } else if (spigotPlayer.getVelocity().getY() > 0) {
                        // jumping
                        if (spigotPlayer.isSprinting())
                        velocity[0] = (velocity[0] * slipperinessMultiplier[0] * 0.91) + (0.1 * movementMultiplier[0] * effectMultiplier[0] * Math.pow((0.6/slipperinessMultiplier[0]),3)) * Math.sin(spigotPlayer.getVelocity()) + 0.2;
                        else
                            velocity[0] = (velocity[0] * slipperinessMultiplier[0] * 0.91) + (0.1 * movementMultiplier[0] * effectMultiplier[0] * Math.pow((0.6/slipperinessMultiplier[0]),3));
                    } else if (spigotPlayer.isFlying()) {
                        velocity[0] = (velocity[0] * slipperinessMultiplier[0] + 0.91) + (0.02 * movementMultiplier[0]);
                    }
