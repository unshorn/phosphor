package edu.columbia.cs.psl.phosphor.struct;

import edu.columbia.cs.psl.phosphor.Configuration;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class LazyFloatArrayObjTags extends LazyArrayObjTags {

    private static final long serialVersionUID = 6150577887427835055L;

    public float[] val;

    public LazyFloatArrayObjTags(int len) {
        val = new float[len];
    }

    public LazyFloatArrayObjTags(float[] array, Taint[] taints) {
        this.taints = taints;
        this.val = array;
    }

    public LazyFloatArrayObjTags(float[] array) {
        this.val = array;
    }

    public LazyFloatArrayObjTags(Taint lenTaint, float[] array) {
        this.val = array;
        this.lengthTaint = lenTaint;
    }

    public static LazyFloatArrayObjTags factory(Taint referenceTaint, float[] array) {
        if (array == null) {
            return null;
        }
        return new LazyFloatArrayObjTags(referenceTaint, array);
    }

    @Override
    public Object clone() {
        return new LazyFloatArrayObjTags(val.clone(), (taints != null) ? taints.clone() : null);
    }

    public void set(Taint referenceTaint, int idx, Taint idxTag, float val, Taint tag) {
        set(idx, val, Configuration.derivedTaintListener.arraySet(referenceTaint, this, idxTag, idx, tag, val, null));
    }

    public void set(int idx, float val, Taint tag) {
        this.val[idx] = val;
        if (taints == null && tag != null && !tag.isEmpty()) {
            taints = new Taint[this.val.length];
        }
        if (taints != null) {
            taints[idx] = tag;
        }
    }

    public void set(Taint referenceTaint, int idx, Taint idxTag, float val, Taint tag, ControlTaintTagStack ctrl) {
        checkAIOOB(idxTag, idx, ctrl);
        set(idx, val, Configuration.derivedTaintListener.arraySet(referenceTaint, this, idxTag, idx, tag, val, ctrl));
    }

    public TaintedFloatWithObjTag get(Taint referenceTaint, int idx, Taint idxTaint, TaintedFloatWithObjTag ret) {
        return Configuration.derivedTaintListener.arrayGet(this, idxTaint, idx, ret, null);
    }

    public TaintedFloatWithObjTag get(Taint referenceTaint, int idx, Taint idxTaint, TaintedFloatWithObjTag ret, ControlTaintTagStack ctrl) {
        checkAIOOB(idxTaint, idx, ctrl);
        return Configuration.derivedTaintListener.arrayGet(this, idxTaint, idx, ret, ctrl);
    }

    public TaintedFloatWithObjTag get(int idx, TaintedFloatWithObjTag ret) {
        ret.val = val[idx];
        ret.taint = (taints == null) ? Taint.emptyTaint() : taints[idx];
        return ret;
    }

    public TaintedFloatWithObjTag get(int idx, TaintedFloatWithObjTag ret, ControlTaintTagStack ctrl) {
        checkAIOOB(null, idx, ctrl);
        get(idx, ret);
        ret.taint = Taint.combineTags(ret.taint, ctrl);
        return ret;
    }

    public int getLength() {
        return val.length;
    }

    @Override
    public Object getVal() {
        return val;
    }

    public void ensureVal(float[] v) {
        if (v != val) {
            val = v;
        }
    }

    public static float[] unwrap(LazyFloatArrayObjTags obj) {
        if (obj != null) {
            return obj.val;
        }
        return null;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        if (val == null) {
            stream.writeInt(-1);
        } else {
            stream.writeInt(val.length);
            for (float el : val) {
                stream.writeFloat(el);
            }
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        int len = stream.readInt();
        if (len == -1) {
            val = null;
        } else {
            val = new float[len];
            for (int i = 0; i < len; i++) {
                val[i] = stream.readFloat();
            }
        }
    }
}


