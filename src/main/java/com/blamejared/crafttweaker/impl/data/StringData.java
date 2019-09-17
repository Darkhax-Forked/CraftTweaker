package com.blamejared.crafttweaker.impl.data;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.data.IData;
import net.minecraft.nbt.StringNBT;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("crafttweaker.api.data.StringData")
@ZenRegister
public class StringData implements IData {
    
    private StringNBT internal;
    
    public StringData(StringNBT internal) {
        this.internal = internal;
    }
    
    @ZenCodeType.Constructor
    public StringData(String internal) {
        this.internal = new StringNBT(internal);
    }
    
    @Override
    public IData copy() {
        return new StringData(internal);
    }
    
    @Override
    public StringNBT getInternal() {
        return internal;
    }
    
    
    @Override
    public boolean contains(IData data) {
        return asString().equals(data.asString());
    }
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.ADD)
    public StringData addTogether(StringData data) {
        return new StringData(internal.getString() + " and " + data.internal.getString());
    }
    
    @Override
    public String asString() {
        return quoteAndEscape(internal.getString()) + " as string";
    }
    
    private String quoteAndEscape(String p_193588_0_) {
        StringBuilder stringbuilder = new StringBuilder("\"");
        
        for(int i = 0; i < p_193588_0_.length(); ++i) {
            char c0 = p_193588_0_.charAt(i);
            
            if(c0 == '\\' || c0 == '"') {
                stringbuilder.append('\\');
            }
            
            stringbuilder.append(c0);
        }
        
        return stringbuilder.append('"').toString();
    }
}