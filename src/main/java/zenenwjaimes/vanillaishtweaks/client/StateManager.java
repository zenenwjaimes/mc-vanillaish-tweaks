package zenenwjaimes.vanillaishtweaks.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class StateManager {
    private int thrownFromSlot;

    private static StateManager instance;

    private StateManager() {
        thrownFromSlot = -1;
    }

    public static StateManager getInstance() {
        if (instance == null) {
            synchronized (StateManager.class) {
                if (instance == null) {
                    instance = new StateManager();
                }
            }
        }
        return instance;
    }

    public int getThrownFromSlot() {
        return thrownFromSlot;
    }

    public void setThrownFromSlot(int thrownFromSlot) {
        this.thrownFromSlot = thrownFromSlot;
    }
}
