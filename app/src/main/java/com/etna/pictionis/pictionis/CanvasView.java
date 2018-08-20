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
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;


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
    Map<String, Point> TBLPoints = new HashMap<>();
    //ArrayList<Float> TBLPoints = new ArrayList<>();


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
                Map<String, HashMap> map = (Map<String, HashMap>) dataSnapshot.getValue(); //for map
                //Map valeurs = (HashMap) dataSnapshot.getValue(); // Choper les données quand elles changes coté firebase
                //ArrayList<Float> recep = (ArrayList<Float>) valeurs;
                if (map != null) {
                    //System.out.println(TblxPoints.getParent().toString());



                    //TODO: écrire ce qui est reçu sur le canvas
                    System.out.println("DEBUG" + map.size());

                    for (Map.Entry<String, HashMap> mapentry : map.entrySet()) {
                        Point point = new Point(mapentry.getValue());
                        float x = (float) point.X;
                        float y = (float) point.Y;
                        System.out.println("DEBUG" + mapentry);
                        String type = (String) mapentry.getValue().get("type");


//                        String type = point.type;
                        System.out.println("TYPE : " + type);
                        //Point point = (Point) mapentry.getValue();
                        //HashMap point = mapentry.getValue();

                        switch (type) {
                            case "start":
                                mPath.reset();
                                mPath.moveTo(x,y);
//                                startTouchLocal(x, y);
//                                invalidate();
                                break;
                            case "move":
                                  mPath.lineTo(x,y);
//                                moveTouchLocal(x, y);
//                                invalidate();
                                break;
                            case "up":
                                upTouchLocal();
                                invalidate();
                                break;
                        }

                        //System.out.println("clé: "+mapentry.getKey() + " | valeur: " + mapentry.getValue());
//                        System.out.println("point X: "+point.X);
//                        System.out.println("point Y: "+point.Y);

                        //startTouch((float)point.X,(float)point.Y);
                        //moveTouchLocal((float)point.X,(float)point.Y);

                        //HashMap point = (HashMap) mapentry.getValue();
                        //for (Object k : point.entrySet()){

                        //}

                    }

                    //for (Object n : list){
                        //Ecriture sur le canvas
                      //  System.out.println(n);
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Cancelled");
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
        System.out.println("ON Draw");
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        canvas.drawPath(mPath, mPaint);
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        System.out.println("START TOUCH");
        //mPath.moveTo(x, y);

        Point point = new Point();

        point.X = (double) x;
        point.Y = (double) y;
        point.type = "start";

        DatabaseReference newPostRef = TblxPoints.push();
        String id = newPostRef.getKey();
        TBLPoints.put(id,point);
        TblxPoints.setValue(TBLPoints);
    }

    private void startTouchLocal(float x, float y) {
        System.out.println("START TOUCH");
        mPath.moveTo(x, y);
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        System.out.println("Move TOUCH");

//        if (dx >= TOLERANCE || dy >= TOLERANCE) {
//            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
//            mX = x;
//            mY = y;
//        }
        Point point = new Point();

        point.X = (double) x;
        point.Y = (double) y;
        point.type = "move";
        DatabaseReference newPostRef = TblxPoints.push();
        String id = newPostRef.getKey();
        TBLPoints.put(id,point);
        TblxPoints.setValue(TBLPoints);
    }

    private void moveTouchLocal(float x, float y) {
        System.out.println(x);
        System.out.println(y);
        mPath.lineTo(x,y);
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
//        System.out.println("UP TOUCH");
    }

    private void upTouchLocal() {
        System.out.println("UP TOUCH");
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
