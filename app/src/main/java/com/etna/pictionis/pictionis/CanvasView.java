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

import java.util.ArrayList;


public class CanvasView extends View {

    public int width;
    public  int height;
    private Bitmap  mBitmap;
    private Canvas  canvas;
    private Path    mPath;
    private Paint   mPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference TblxPath;
    private String partyName;
    ArrayList<Path> TBLPath = new ArrayList<>();


    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        DBref = FirebaseDatabase.getInstance().getReference();

        circlePaint = new Paint();
        circlePath = new Path();
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

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        DatabaseReference newPostRef = TblxPath.push();
        TblxPath.setValue(mPath);
    }



    private void touchLocal(Path path) {
        canvas.drawPath(path, mPaint);
    }

    public void clearRemoteCanvas() {
        TblxPath.removeValue();
        TBLPath.clear();

    }

    public void clearLocalCanvas(){
        mPath.reset();
        circlePath.reset();
        canvas.drawColor(Color.WHITE);
        invalidate();
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //System.out.println("ON TOUCH EVENT");
        float x = event.getX();
        float y = event.getY();

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
        return true;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;

        System.out.println("---------------------fsdfsd--------------------------");
        System.out.println(this.partyName);
        System.out.println("------------------------sdffsd-----------------------");
        this.TblxPath.child(this.partyName).push().setValue(1);

        this.TblxPath =  this.TblxPath.child(this.partyName);

        TblxPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Path path = snapshot.getValue(Path.class);

                    touchLocal(path);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Cancelled");
            }
        });
    }
}
