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
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.etna.pictionis.pictionis.R.id.canvas;


public class CanvasView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    DatabaseReference DBref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference TblxPoints = DBref.child("Points");
    //Map<String, Float> TBLPoints = new HashMap<String, Float>();
    ArrayList<Float> TBLPoints = new ArrayList<>();


    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;


        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        DBref = FirebaseDatabase.getInstance().getReference();

        TblxPoints.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue(); for map
                Object valeurs = dataSnapshot.getValue(); // Choper les données quand elles changes coté firebase
                if (valeurs != null) {
                    //System.out.println(TblxPoints.getParent().toString());
                    //Map list = (Map) valeurs;
                    System.out.println(valeurs);

                    //TODO: écrire ce qui est reçu sur le canvas

                    //for (Object n : list)
                    //    Ecriture sur le canvas
                    //    System.out.println(n.toString());
                    //}
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        //System.out.println("OnDraw");
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDrawc
        canvas.drawPath(mPath, mPaint);
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        //System.out.println("START TOUCH");
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
        //TBLPoints.put("X",mX); with map
        //TBLPoints.put("Y",mY); with map
        TBLPoints.add(mX);
        TBLPoints.add(mY);
        TblxPoints.setValue(TBLPoints);
    }

    public void clearCanvas() {
        //System.out.println("CLEAR CANEVAS");
        mPath.reset();
        TblxPoints.removeValue();
        //TBLPoints.remove(TBLPoints); with map
        TBLPoints.clear();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        //System.out.println("UP TOUCH");
        mPath.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //System.out.println("ON TOUCH EVENT");
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}
