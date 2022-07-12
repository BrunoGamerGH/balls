    public boolean isBetweenYaws(float bodyYaw, float yaw1, float yaw2) {
        // check if its +/+ or -/-
        if (yaw1 >= 0 && yaw2 >=0) {
            // its +/+
            if (yaw1 > yaw2) {
                return bodyYaw < yaw1 && bodyYaw > yaw2;
            } else if (yaw1 < yaw2) {
                return bodyYaw < yaw2 && bodyYaw > yaw1;
            } else {
                return false;
            }

        } else if (yaw1 < 0 && yaw2 < 0) {
            // its -/-
            if (yaw1 > yaw2) {
                return bodyYaw < yaw1 && bodyYaw > yaw2;
            } else if (yaw1 < yaw2) {
                return bodyYaw < yaw2 && bodyYaw > yaw1;
            } else {
                return false;
            }
        } else if (yaw1 >= 0 && yaw2 < 0) {
            // its +/-
            if (yaw1 < 90) {
                // yaw1 is smaller thank 90
                if (yaw2 < -90) {
                    // yaw2 is smaller than -90
                    return (bodyYaw < yaw1 && bodyYaw > 0) || (bodyYaw < yaw2 && bodyYaw > -180);
                } else {
                    // yaw2 is bigger than -90
                    return (bodyYaw < yaw1 && bodyYaw > 0) || (bodyYaw > yaw2 && bodyYaw < 0);
                }
            } else {
                // yaw1 is bigger than 90
                if (yaw2 < -90) {
                    // yaw2 is smaller than -90
                    return (bodyYaw < yaw1 && bodyYaw > 90) || (bodyYaw < yaw2 && bodyYaw > -180);
                } else {
                    // yaw2 is bigger than -90
                    return (bodyYaw < yaw1 && bodyYaw > 90) || (bodyYaw > yaw2 && bodyYaw < 0);
                }
            }
        } else if (yaw1 < 0 && yaw2 >= 0) {
            // its -/+
            if (yaw1 < -90) {
                // yaw1 is smaller thank 90
                if (yaw2 < 90) {
                    // yaw2 is smaller than -90
                    return (bodyYaw < yaw1 && bodyYaw > -180) || (bodyYaw < yaw2 && bodyYaw > 0);
                } else {
                    // yaw2 is bigger than -90
                    return (bodyYaw < yaw1 && bodyYaw > -180) || (bodyYaw < yaw2 && bodyYaw > 180);
                }
            } else {
                // yaw1 is bigger than 90
                if (yaw2 < 90) {
                    // yaw2 is smaller than -90
                    return (bodyYaw > yaw1 && bodyYaw < 0) || (bodyYaw < yaw2 && bodyYaw > 0);
                } else {
                    // yaw2 is bigger than -90
                    return (bodyYaw > yaw1 && bodyYaw < 0) || (bodyYaw < yaw2 && bodyYaw > 180);
                }
            }
        }

        return false;
    }
