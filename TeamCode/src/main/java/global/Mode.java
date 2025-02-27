package global;

import java.util.HashMap;


public class Mode {
    /**
     * Class for creating enums that have a value
     */

    /**
     * ModeType enum to represent the different values of the mode
     */


    private final HashMap<ModeType, Double> valueMap = new HashMap<>();

    private ModeType currentMode;
    private final ModeType[] types;

    public Mode(Class<? extends Enum<? extends ModeType>> types){
        this.types = (ModeType[]) types.getEnumConstants();
        currentMode = this.types[0];
    }

    public Mode set(ModeType type, double value){
        valueMap.put(type, value);
        return this;
    }


    public void cycleUp() { for (int i = 0; i < types.length-1; i++) { if(modeIs(types[i])){ set(types[i+1]); return; }} if(modeIs(types[types.length-1])){set(types[0]);} }
    public void cycleDown() { for (int i = 1; i < types.length; i++) { if(modeIs(types[i])){ set(types[i-1]); return; }} if(modeIs(types[0])){set(types[types.length-1]);} }
    public boolean modeIs(ModeType other){ return currentMode.equals(other); }
    public BooleanReturnCodeSeg isMode(ModeType other){return () -> currentMode.equals(other); }





    public ModeType get(){ return currentMode; }
    public void set(ModeType mode){ this.currentMode = mode; }

    public void toggle(ModeType one, ModeType two){ if(modeIs(one)){set(two);}else{set(one);} }

    public interface ModeType{};

    @FunctionalInterface
    public interface BooleanReturnCodeSeg extends ReturnCodeSeg<Boolean> {
        default BooleanReturnCodeSeg and(BooleanReturnCodeSeg other){ return () -> this.run() && other.run(); }
    }

}
