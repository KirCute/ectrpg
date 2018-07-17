package com.ectrpg.view.dialog;

import com.ectrpg.controller.service.Resource;
import com.ectrpg.db.Localization;
import com.ectrpg.model.entity.FriendlyNPC;
import com.ectrpg.view.dialog.obj.TimerText;
import org.frice.obj.AttachedAbstractObjects;
import org.frice.obj.AttachedObjects;
import org.frice.obj.FObject;
import org.frice.obj.button.FText;
import org.frice.obj.button.SimpleText;
import org.frice.obj.sub.ShapeObject;
import org.frice.resource.graphics.ColorResource;
import org.frice.util.shape.FRectangle;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NpcGreets implements IDialog, IManager, Serializable {
    public static final int OBJECTS_X = 0;
    public static final int LINE_HEIGHT = 10;
    public static final int TEXT_SIZE = 10;
    private transient AttachedObjects bottom;
    private transient AttachedAbstractObjects dialog;
    private transient ShapeObject table;
    private transient List<FText> textObj;
    private transient List<TimerText> tt;
    private List<String> s;
    private int shift_x;
    private int shift_y;
    private int width;
    private int height;
    private FObject masterView;
    private FriendlyNPC master;

    public NpcGreets(@NotNull FriendlyNPC master, int width, int height, int shift_x, int shift_y, @NotNull List<String> s) {
        this.master = master;
        this.width = width;
        this.height = height;
        this.shift_x = shift_x;
        this.shift_y = shift_y;
        this.s = s;
    }

    @Override
    public AttachedAbstractObjects getTopAttachedObjects() {
        init();
        if (this.dialog != null) {
            return dialog;
        }
        this.dialog = new AttachedAbstractObjects(new ArrayList<>());
        for (FText ft : textObj) {
            this.dialog.addObject(ft);
        }
        return dialog;
    }

    @Override
    public AttachedObjects getBottomAttachedObjects() {
        init();
        if (this.bottom != null) {
            return bottom;
        }
        this.bottom = new AttachedObjects(new ArrayList<>());
        this.bottom.addObject(table);
        return bottom;
    }

    @Override
    public int getResource() {
        return -1;
    }

    @Override
    public double getX() {
        init();
        try {
            return table.getX();
        } catch (NullPointerException npe) {
            return masterView.getX() + this.shift_x;
        }
    }

    @Override
    public double getY() {
        init();
        try {
            return table.getY();
        } catch (NullPointerException npe) {
            return masterView.getY() + this.shift_y;
        }
    }

    @Override
    public void onRefresh() {
        init();
        try {
            this.table.setX(masterView.getX() + this.shift_x);
            this.table.setY(masterView.getY() + this.shift_y);
            for (int i = 0; i < textObj.size(); i++) {
                textObj.get(i).setX(table.getX() + OBJECTS_X);
                textObj.get(i).setY(table.getY() + LINE_HEIGHT * (i + 1));
            }
            for (TimerText t : tt) {
                t.onRefresh();
            }
        } catch (NullPointerException ignored) { }
    }

    @Override
    public void startCome() {
        init();
        tt.get(0).startCome();
    }

    private void init() {
        if (this.masterView == null) {
            this.masterView = Resource.getEntitiesImage().get(Resource.getId(master));this.tt = new ArrayList<>();
            this.textObj = new ArrayList<>();
            this.table = new ShapeObject(new ColorResource(0, 0, 0, 127), new FRectangle(width, height));
            for (String str : s) {
                SimpleText st = new SimpleText(ColorResource.WHITE, "",
                        table.getX() + OBJECTS_X, table.getY() + LINE_HEIGHT * (this.tt.size() + 1));
                st.setTextSize(TEXT_SIZE);
                st.setFontName(Localization.getFont());
                this.textObj.add(st);
                this.tt.add(new TimerText(str, textObj.get(textObj.size() - 1)));
            }
            for (int i = 0; i < this.tt.size() - 1; i++) {
                this.tt.get(i).init(this.tt.get(i + 1));
                this.tt.get(i).setGroup(this);
            }
            this.tt.get(this.tt.size() - 1).setGroup(this);
        }
    }

    @Override
    public void end() {

    }
}
