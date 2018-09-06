package com.etna.pictionis.pictionis;

/**
 * Created by Zouclar on 18/06/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Random;


public class CanvasView extends View {

    public int width;
    public  int height;
    private Boolean isHost;
    private Bitmap  mBitmap;
    private Canvas  canvas;
    private CustomPath    mPath;
    private Paint   mPaint;
    Context context;
    private Paint circlePaint;
    private CustomPath circlePath;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference TblxPath;
    private String partyName;
    ArrayList<Path> TBLPath = new ArrayList<>();


    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        mPath = new CustomPath(new ArrayList<PathAction>());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        DBref = FirebaseDatabase.getInstance().getReference();

        circlePaint = new Paint();
        circlePath = new CustomPath(new ArrayList<PathAction>());
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(mBitmap);;
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mPaint);
        canvas.drawPath( mPath,  mPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    //When touch screen
    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    //When move on screen
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            if(isHost){
                Gson gson = new Gson();
                String path_json = gson.toJson(mPath.actions);
                TblxPath.setValue(path_json);
            }
        }
    }

    //When up of screen
    private void touch_up() {
        mPath.lineTo(mX, mY);

        if(isHost){
            Gson gson = new Gson();
            String path_json = gson.toJson(mPath.actions);
            TblxPath.setValue(path_json);
        }
    }

    //Clean Canvas of Firebase
    public void clearRemoteCanvas() {
        TblxPath.removeValue();
        TBLPath.clear();
    }

    //Clean Canvas local
    public void clearLocalCanvas(){
        mPath.reset();
        mPath.actions = new ArrayList<PathAction>();
        circlePath.reset();
        canvas.drawColor(Color.WHITE);
        invalidate();
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if(isHost){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
        }
        return true;
    }

    //Set name of party
    public void setPartyName(String partyName, Boolean isHost) {
        this.isHost = isHost;
        if(isHost){
            Random rand = new Random();
            this.partyName = partyName.replaceAll("\\s+","") + "-" + rand.nextInt(10000) + 1;
            DBref = DBref.child(this.partyName);
            TblxPath = DBref.child("path");
            TblxPath.setValue(this.partyName);
        }
        else{
            this.partyName = partyName;
            DBref = DBref.child(this.partyName);
            TblxPath = DBref;
        }

        if(!this.isHost){
            TblxPath.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Gson gson = new Gson();
                        String jsonPathAction = snapshot.getValue().toString();
                        ArrayList<PathAction> actions = new ArrayList<PathAction>();

                        actions = gson.fromJson(jsonPathAction, new TypeToken<ArrayList<PathAction>>(){}.getType());


                        if(!mPath.actions.isEmpty()){
                            mPath.reset();
                            mPath.actions = new ArrayList<PathAction>();
                            circlePath.reset();
                            canvas.drawColor(Color.WHITE);
                            invalidate();
                        }

                        for(PathAction p : actions){
                            if(p.getType().equals("MOVE_TO")) {
                                touch_start(p.getX(), p.getY());
                                invalidate();
                            }
                            else if(p.getType().equals("LINE_TO")){
                                touch_up();
                                invalidate();
                            }
                            else if(p.getType().equals("QUAD_TO")){
                                touch_move(p.getX(), p.getY());
                                invalidate();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("Cancelled");
                }
            });
        }
    }
}