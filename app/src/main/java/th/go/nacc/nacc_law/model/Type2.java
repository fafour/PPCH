package th.go.nacc.nacc_law.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by error on 5/3/2017 AD.
 */

public class Type2 implements Serializable{
    public String Name;
    public String id;
    public ArrayList<Type2> SubType = new ArrayList<>();
}
