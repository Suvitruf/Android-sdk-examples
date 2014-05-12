![FlowLayout example](https://raw.github.com/Suvitruf/Android-sdk-examples/master/FlowLayout/FlowLayout.png)
## Overview ##
# Attributes #
```.xml
<declare-styleable name="FlowLayout">
       <attr name="paddingV" format="string" />
       <attr name="paddingH" format="string" />
   </declare-styleable>

```

# Usage #
```.xml
 <ru.suvitruf.flowlayoutexample.view.FlowLayout
        android:id="@+id/test_flow_layout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        t:paddingH="@integer/test_padding_h"
        t:paddingV="@integer/test_padding_v" >
    </ru.suvitruf.flowlayoutexample.view.FlowLayout>

```

[Suvitruf](http://suvitruf.ru/2013/11/17/3396/)
