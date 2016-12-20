FreeDrawView
======

<img src="TODO sample image" title="sample" />

A View that let you draw freely on it. You can customize paint width, alpha and color.
Can be useful for notes app, signatures or hands-free writing<br />
This View works flawlessly inside Scrolling parents like NestedScrollView. <br />
Also supports state-restore on rotation, with custom behaviours like "clear, crop or fitXY" <br />
and you can take a screenshot (given to you as a Bitmap Object) of the View drawn content<br />

<br />
You can try the demo app on google play store. <br />
TODO link <br />

Download
------
####Gradle:
```groovy
TODO artifact
```

## Usage

To use this library, just add this inside your layout file

```xml
    <com.rm.freedraw.FreeDrawView
                    android:id="@+id/your_id"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:background="@color/white"

                    app:paintColor="@color/black"
                    app:paintWidth="4dp"
                    app:resizeBehaviour="crop"/>
```

... if you need to use this View's custom xml attributes (shown in a table below or in the example above) do not forget to add this to your root layout
```
xmlns:app="http://schemas.android.com/apk/res-auto"
```

And this in your Activity
```java
public class MainActivity extends AppCompatActivity {
    FreeDrawView mSignatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignatureView = (FreeDrawView) findViewById(R.id.your_id);

        // Setup the View
        mSignatureView.setPaintColor(Color.BLACK);
        mSignatureView.setPaintWidthPx(getResources.getDimensionPixelSize(R.dimen.paint_width));
        //mSignatureView.setPaintWidthPx(12);
        //TODO other attributes
    }
}
```

//TODO XML attributes

License
--------

    Copyright 2016 Riccardo Moro.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
