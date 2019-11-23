package edu.columbia.cs.psl.phosphor.runtime;

import edu.columbia.cs.psl.phosphor.TaintUtils;
import edu.columbia.cs.psl.phosphor.struct.*;
import edu.columbia.cs.psl.phosphor.struct.harmony.util.WeakHashMap;
import edu.columbia.cs.psl.phosphor.struct.multid.MultiDTaintedArrayWithObjTag;
import org.objectweb.asm.Type;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class RuntimeReflectionPropagator {

    private static Unsafe unsafe;
    private static WeakHashMap<Field, Field> fieldToField = new WeakHashMap<>();

    private RuntimeReflectionPropagator() {
        // Prevents this class from being instantiated
    }

    private static Unsafe getUnsafe() {
        if(unsafe == null) {
            unsafe = Unsafe.getUnsafe();
        }
        return unsafe;
    }

    public static Class<?> getType$$PHOSPHORTAGGED(Field f, ControlTaintTagStack ctrl) {
        return getType(f);
    }

    public static Class<?> getType(Field f) {
        String name = f.getName();
        if(f.getName().endsWith("PHOSPHOR_TAG")) {
            return f.getType();
        }
        Class<?> ret = f.getType();
        Class<?> component = ret;
        while(component.isArray()) {
            component = component.getComponentType();
        }
        if(LazyArrayObjTags.class.isAssignableFrom(component)) {
            String d = Type.getDescriptor(ret);

            String newType = "[";
            char[] c = d.toCharArray();
            for(int i = 0; i < c.length && c[i] == '['; i++) {
                newType += "[";
            }
            newType += MultiDTaintedArrayWithObjTag.getPrimitiveTypeForWrapper(component);
            try {
                ret = Class.forName(newType);
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static Object get$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        return get(f, obj);
    }

    public static Object get(Field f, Object obj) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        Object ret;
        if(f.getType().isPrimitive()) {
            if(f.getType() == Boolean.TYPE) {
                ret = getBoolean$$PHOSPHORTAGGED(f, obj, new TaintedBooleanWithObjTag());
            } else if(f.getType() == Byte.TYPE) {
                ret = getByte$$PHOSPHORTAGGED(f, obj, new TaintedByteWithObjTag());
            } else if(f.getType() == Character.TYPE) {
                ret = getChar$$PHOSPHORTAGGED(f, obj, new TaintedCharWithObjTag());
            } else if(f.getType() == Double.TYPE) {
                ret = getDouble$$PHOSPHORTAGGED(f, obj, new TaintedDoubleWithObjTag());
            } else if(f.getType() == Float.TYPE) {
                ret = getFloat$$PHOSPHORTAGGED(f, obj, new TaintedFloatWithObjTag());
            } else if(f.getType() == Long.TYPE) {
                ret = getLong$$PHOSPHORTAGGED(f, obj, new TaintedLongWithObjTag());
            } else if(f.getType() == Integer.TYPE) {
                ret = getInt$$PHOSPHORTAGGED(f, obj, new TaintedIntWithObjTag());
            } else if(f.getType() == Short.TYPE) {
                ret = getShort$$PHOSPHORTAGGED(f, obj, new TaintedShortWithObjTag());
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            ret = f.get(obj);
        }
        if(f.getType().isArray() && f.getType().getComponentType().isPrimitive()) {
            Object taint = null;

            try {
                try {
                    if(ret instanceof LazyArrayObjTags && ((LazyArrayObjTags) ret).taints != null) {
                        return ret;
                    } else {
                        Field taintField;
                        if(fieldToField.containsKey(f)) {
                            taintField = fieldToField.get(f);
                        } else {
                            taintField = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                            taintField.setAccessible(true);
                            fieldToField.put(f, taintField);
                        }
                        taint = taintField.get(obj);
                        return taint;
                    }
                } catch(NoSuchFieldException t) {

                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if(ret instanceof TaintedPrimitiveWithObjTag) {
            return ((TaintedPrimitiveWithObjTag) ret).toPrimitiveType();
        }
        return ret;
    }

    public static TaintedBooleanWithObjTag getBoolean$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl, TaintedBooleanWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        return getBoolean$$PHOSPHORTAGGED(f, obj, ret);
    }

    public static TaintedByteWithObjTag getByte$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl, TaintedByteWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        return getByte$$PHOSPHORTAGGED(f, obj, ret);
    }

    public static TaintedCharWithObjTag getChar$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl, TaintedCharWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        return getChar$$PHOSPHORTAGGED(f, obj, ret);
    }

    public static TaintedDoubleWithObjTag getDouble$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl, TaintedDoubleWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        return getDouble$$PHOSPHORTAGGED(f, obj, ret);
    }

    public static TaintedFloatWithObjTag getFloat$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl, TaintedFloatWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        return getFloat$$PHOSPHORTAGGED(f, obj, ret);
    }

    public static TaintedIntWithObjTag getInt$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl, TaintedIntWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        return getInt$$PHOSPHORTAGGED(f, obj, ret);
    }

    public static TaintedLongWithObjTag getLong$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl, TaintedLongWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        return getLong$$PHOSPHORTAGGED(f, obj, ret);
    }

    public static TaintedShortWithObjTag getShort$$PHOSPHORTAGGED(Field f, Object obj, ControlTaintTagStack ctrl, TaintedShortWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        return getShort$$PHOSPHORTAGGED(f, obj, ret);
    }

    public static TaintedBooleanWithObjTag getBoolean$$PHOSPHORTAGGED(Field f, Object obj, TaintedBooleanWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        ret.val = f.getBoolean(obj);
        try {
            Field taintField;
            if(fieldToField.containsKey(f)) {
                taintField = fieldToField.get(f);
            } else {
                taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
                taintField.setAccessible(true);
                fieldToField.put(f, taintField);
            }
            Object t = taintField.get(obj);
            if(t instanceof Taint) {
                ret.taint = (Taint) t;
            }
            if(t instanceof Integer) {
                ret.taint = (Taint) HardcodedBypassStore.get(((Integer) t).intValue());
            }
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static TaintedByteWithObjTag getByte$$PHOSPHORTAGGED(Field f, Object obj, TaintedByteWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        ret.val = f.getByte(obj);
        try {
            Field taintField;
            if(fieldToField.containsKey(f)) {
                taintField = fieldToField.get(f);
            } else {
                taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
                taintField.setAccessible(true);
                fieldToField.put(f, taintField);
            }
            Object t = taintField.get(obj);
            if(t instanceof Taint) {
                ret.taint = (Taint) t;
            }
            if(t instanceof Integer) {
                ret.taint = (Taint) HardcodedBypassStore.get(((Integer) t).intValue());
            }
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static TaintedCharWithObjTag getChar$$PHOSPHORTAGGED(Field f, Object obj, TaintedCharWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        ret.val = f.getChar(obj);
        try {
            Field taintField;
            if(fieldToField.containsKey(f)) {
                taintField = fieldToField.get(f);
            } else {
                taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
                taintField.setAccessible(true);
                fieldToField.put(f, taintField);
            }
            Object t = taintField.get(obj);
            if(t instanceof Taint) {
                ret.taint = (Taint) t;
            }
            if(t instanceof Integer) {
                ret.taint = (Taint) HardcodedBypassStore.get(((Integer) t).intValue());
            }
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static TaintedDoubleWithObjTag getDouble$$PHOSPHORTAGGED(Field f, Object obj, TaintedDoubleWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        ret.val = f.getDouble(obj);
        try {
            Field taintField;
            if(fieldToField.containsKey(f)) {
                taintField = fieldToField.get(f);
            } else {
                taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
                taintField.setAccessible(true);
                fieldToField.put(f, taintField);
            }
            Object t = taintField.get(obj);
            if(t instanceof Taint) {
                ret.taint = (Taint) t;
            }
            if(t instanceof Integer) {
                ret.taint = (Taint) HardcodedBypassStore.get(((Integer) t).intValue());
            }
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static TaintedFloatWithObjTag getFloat$$PHOSPHORTAGGED(Field f, Object obj, TaintedFloatWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        ret.val = f.getFloat(obj);
        try {
            Field taintField;
            if(fieldToField.containsKey(f)) {
                taintField = fieldToField.get(f);
            } else {
                taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
                taintField.setAccessible(true);
                fieldToField.put(f, taintField);
            }
            Object t = taintField.get(obj);
            if(t instanceof Taint) {
                ret.taint = (Taint) t;
            }
            if(t instanceof Integer) {
                ret.taint = (Taint) HardcodedBypassStore.get(((Integer) t).intValue());
            }
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static TaintedIntWithObjTag getInt$$PHOSPHORTAGGED(Field f, Object obj, TaintedIntWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        ret.val = f.getInt(obj);
        try {
            Field taintField;
            if(fieldToField.containsKey(f)) {
                taintField = fieldToField.get(f);
            } else {
                taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
                taintField.setAccessible(true);
                fieldToField.put(f, taintField);
            }
            Object t = taintField.get(obj);
            if(t instanceof Taint) {
                ret.taint = (Taint) t;
            }
            if(t instanceof Integer) {
                ret.taint = (Taint) HardcodedBypassStore.get(((Integer) t).intValue());
            }

        } catch(SecurityException e) {
            e.printStackTrace();
        } catch(NoSuchFieldException e) {
            //
        }
        return ret;
    }

    public static TaintedLongWithObjTag getLong$$PHOSPHORTAGGED(Field f, Object obj, TaintedLongWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        ret.val = f.getLong(obj);
        try {
            Field taintField;
            if(fieldToField.containsKey(f)) {
                taintField = fieldToField.get(f);
            } else {
                taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
                taintField.setAccessible(true);
                fieldToField.put(f, taintField);
            }
            Object t = taintField.get(obj);
            if(t instanceof Taint) {
                ret.taint = (Taint) t;
            }
            if(t instanceof Integer) {
                ret.taint = (Taint) HardcodedBypassStore.get(((Integer) t).intValue());
            }
        } catch(NoSuchFieldException | SecurityException e) {
            //
        }
        return ret;
    }

    public static TaintedShortWithObjTag getShort$$PHOSPHORTAGGED(Field f, Object obj, TaintedShortWithObjTag ret) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        ret.val = f.getShort(obj);
        try {
            Field taintField;
            if(fieldToField.containsKey(f)) {
                taintField = fieldToField.get(f);
            } else {
                taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
                taintField.setAccessible(true);
                fieldToField.put(f, taintField);
            }
            Object t = taintField.get(obj);
            if(t instanceof Taint) {
                ret.taint = (Taint) t;
            }
            if(t instanceof Integer) {
                ret.taint = (Taint) HardcodedBypassStore.get(((Integer) t).intValue());
            }
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void setAccessible$$PHOSPHORTAGGED(Field f, int tag, boolean flag) {
        f.setAccessible(flag);
        if(isPrimitiveOrPrimitiveArrayType(f.getType())) {
            try {
                f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD).setAccessible(flag);
            } catch(SecurityException | NoSuchFieldException e) {
                //
            }
        }
    }

    public static void setAccessible$$PHOSPHORTAGGED(Field f, int tag, boolean flag, ControlTaintTagStack ctrl) {
        f.setAccessible(flag);
        if(isPrimitiveOrPrimitiveArrayType(f.getType())) {
            try {
                f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD).setAccessible(flag);
            } catch(SecurityException | NoSuchFieldException e) {
                //
            }
        }
    }

    public static void setAccessible$$PHOSPHORTAGGED(Field f, Taint<?> tag, boolean flag) {
        f.setAccessible(flag);
        if(isPrimitiveOrPrimitiveArrayType(f.getType())) {
            try {
                f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD).setAccessible(flag);
            } catch(SecurityException | NoSuchFieldException e) {
                //
            }
        }
    }

    public static void setAccessible$$PHOSPHORTAGGED(Field f, Taint<?> tag, boolean flag, ControlTaintTagStack ctrl) {
        f.setAccessible(flag);
        if(isPrimitiveOrPrimitiveArrayType(f.getType())) {
            try {
                f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD).setAccessible(flag);
            } catch(SecurityException | NoSuchFieldException e) {
                //
            }
        }
    }

    public static void setBoolean$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, boolean val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        tag = Taint.combineTags(tag, ctrl);
        setBoolean$$PHOSPHORTAGGED(f, obj, tag, val);
    }

    public static void setByte$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, byte val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        tag = Taint.combineTags(tag, ctrl);
        setByte$$PHOSPHORTAGGED(f, obj, tag, val);
    }

    public static void setChar$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, char val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        tag = Taint.combineTags(tag, ctrl);
        setChar$$PHOSPHORTAGGED(f, obj, tag, val);
    }

    public static void setDouble$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, double val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        tag = Taint.combineTags(tag, ctrl);
        setDouble$$PHOSPHORTAGGED(f, obj, tag, val);
    }

    public static void setFloat$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, float val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        tag = Taint.combineTags(tag, ctrl);
        setFloat$$PHOSPHORTAGGED(f, obj, tag, val);
    }

    public static void setInt$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, int val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        tag = Taint.combineTags(tag, ctrl);
        setInt$$PHOSPHORTAGGED(f, obj, tag, val);
    }

    public static void setLong$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, long val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        tag = Taint.combineTags(tag, ctrl);
        setLong$$PHOSPHORTAGGED(f, obj, tag, val);
    }

    public static void setShort$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, short val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        tag = Taint.combineTags(tag, ctrl);
        setShort$$PHOSPHORTAGGED(f, obj, tag, val);
    }

    public static void setBoolean$$PHOSPHORTAGGED(Field f, Object obj, int tag, boolean val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setBoolean(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.setInt(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setByte$$PHOSPHORTAGGED(Field f, Object obj, int tag, byte val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setByte(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.setInt(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setChar$$PHOSPHORTAGGED(Field f, Object obj, int tag, char val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setChar(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.setInt(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setDouble$$PHOSPHORTAGGED(Field f, Object obj, int tag, double val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setDouble(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.setInt(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setFloat$$PHOSPHORTAGGED(Field f, Object obj, int tag, float val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setFloat(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.setInt(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setInt$$PHOSPHORTAGGED(Field f, Object obj, int tag, int val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setInt(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.setInt(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setLong$$PHOSPHORTAGGED(Field f, Object obj, int tag, long val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setLong(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.setInt(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setShort$$PHOSPHORTAGGED(Field f, Object obj, int tag, short val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setShort(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.setInt(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setBoolean$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, boolean val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setBoolean(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.set(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setByte$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, byte val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setByte(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.set(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setChar$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, char val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setChar(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.set(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setDouble$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, double val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setDouble(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.set(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setFloat$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, float val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setFloat(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.set(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setInt$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, int val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setInt(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.set(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setLong$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, long val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setLong(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.set(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void setShort$$PHOSPHORTAGGED(Field f, Object obj, Taint<?> tag, short val) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.setShort(obj, val);
        try {
            Field taintField = f.getDeclaringClass().getField(f.getName() + TaintUtils.TAINT_FIELD);
            taintField.setAccessible(true);
            taintField.set(obj, tag);
        } catch(NoSuchFieldException e) {
            //
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private static Taint getTagObj(Object val) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Object ret = val.getClass().getField("value" + TaintUtils.TAINT_FIELD).get(val);
        if(ret instanceof Integer) {
            ret = HardcodedBypassStore.get(((Integer) ret).intValue());
        }
        return (Taint) ret;
    }

    private static int getTag(Object val) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return val.getClass().getField("value" + TaintUtils.TAINT_FIELD).getInt(val);
    }

    public static void set$$PHOSPHORTAGGED(Field f, Object obj, Object val, ControlTaintTagStack ctrl) throws IllegalArgumentException, IllegalAccessException {
        if(f.getType().isPrimitive()) {
            if(val instanceof Integer && f.getType().equals(Integer.TYPE)) {
                Integer i = (Integer) val;
                f.setAccessible(true);
                f.setInt(obj, i.intValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, Taint.combineTags(getTagObj(val), ctrl));
                } catch(NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Boolean && f.getType().equals(Boolean.TYPE)) {
                Boolean i = (Boolean) val;
                f.setAccessible(true);
                f.setBoolean(obj, i.booleanValue());
                return;
            } else if(val instanceof Byte && f.getType().equals(Byte.TYPE)) {
                Byte i = (Byte) val;
                f.setAccessible(true);
                f.setByte(obj, i.byteValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, Taint.combineTags(getTagObj(val), ctrl));
                } catch(SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Character && f.getType().equals(Character.TYPE)) {
                Character i = (Character) val;
                f.setAccessible(true);
                f.setChar(obj, i.charValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, Taint.combineTags(getTagObj(val), ctrl));
                } catch(SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Double && f.getType().equals(Double.TYPE)) {
                Double i = (Double) val;
                f.setAccessible(true);
                f.setDouble(obj, i.doubleValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, Taint.combineTags(getTagObj(val), ctrl));
                } catch(SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Float && f.getType().equals(Float.TYPE)) {
                Float i = (Float) val;
                f.setAccessible(true);
                f.setFloat(obj, i.floatValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, Taint.combineTags(getTagObj(val), ctrl));
                } catch(SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Long && f.getType().equals(Long.TYPE)) {
                Long i = (Long) val;
                f.setAccessible(true);
                f.setLong(obj, i.longValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, Taint.combineTags(getTagObj(val), ctrl));
                } catch(SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Short && f.getType().equals(Short.TYPE)) {
                Short i = (Short) val;
                f.setAccessible(true);
                f.setShort(obj, i.shortValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, Taint.combineTags(getTagObj(val), ctrl));
                } catch(SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        Taint.combineTagsOnObject(obj, ctrl);
        f.setAccessible(true);
        f.set(obj, val);
        if(f.getType().isArray() && val instanceof LazyArrayObjTags) {
            try {
                Field taintField = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                Unsafe u = getUnsafe();
                u.putObject(obj, u.objectFieldOffset(taintField), val);
            } catch(NoSuchFieldException | SecurityException e) {
                //
            }
        }
    }

    public static void set(Field f, Object obj, Object val) throws IllegalArgumentException, IllegalAccessException {
        if(f.getType().isPrimitive()) {
            if(val instanceof Integer && f.getType().equals(Integer.TYPE)) {
                Integer i = (Integer) val;
                f.setAccessible(true);
                f.setInt(obj, i.intValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, getTagObj(val));
                } catch(NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Boolean && f.getType().equals(Boolean.TYPE)) {
                Boolean i = (Boolean) val;
                f.setAccessible(true);
                f.setBoolean(obj, i.booleanValue());
                return;
            } else if(val instanceof Byte && f.getType().equals(Byte.TYPE)) {
                Byte i = (Byte) val;
                f.setAccessible(true);
                f.setByte(obj, i.byteValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, getTagObj(val));
                } catch(NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Character && f.getType().equals(Character.TYPE)) {
                Character i = (Character) val;
                f.setAccessible(true);
                f.setChar(obj, i.charValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, getTagObj(val));
                } catch(NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Double && f.getType().equals(Double.TYPE)) {
                Double i = (Double) val;
                f.setAccessible(true);
                f.setDouble(obj, i.doubleValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, getTagObj(val));
                } catch(NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Float && f.getType().equals(Float.TYPE)) {
                Float i = (Float) val;
                f.setAccessible(true);
                f.setFloat(obj, i.floatValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, getTagObj(val));
                } catch(NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Long && f.getType().equals(Long.TYPE)) {
                Long i = (Long) val;
                f.setAccessible(true);
                f.setLong(obj, i.longValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, getTagObj(val));
                } catch(NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                return;
            } else if(val instanceof Short && f.getType().equals(Short.TYPE)) {
                Short i = (Short) val;
                f.setAccessible(true);
                f.setShort(obj, i.shortValue());
                try {
                    Field tf = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                    tf.setAccessible(true);
                    tf.set(obj, getTagObj(val));
                } catch(NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        f.setAccessible(true);
        f.set(obj, val);
        if(f.getType().isArray() && val instanceof LazyArrayObjTags) {
            try {
                Field taintField = f.getDeclaringClass().getDeclaredField(f.getName() + TaintUtils.TAINT_FIELD);
                Unsafe u = getUnsafe();
                if(Modifier.isStatic(f.getModifiers())) {
                    u.putObject(u.staticFieldBase(taintField), u.staticFieldOffset(taintField), val);
                } else {
                    u.putObject(obj, u.objectFieldOffset(taintField), val);
                }
            } catch(NoSuchFieldException | SecurityException e) {
                //
            }
        }
    }

    private static int getNumberOfDimensions(Class<?> clazz) {
        String name = clazz.getName();
        return name.lastIndexOf('[') + 1;
    }

    private static boolean isPrimitiveOrPrimitiveArrayType(Class<?> clazz) {
        if(clazz.isPrimitive()) {
            return true;
        }
        if(clazz.isArray() && getNumberOfDimensions(clazz) == 1) {
            return isPrimitiveOrPrimitiveArrayType(clazz.getComponentType());
        }
        return false;
    }
}
