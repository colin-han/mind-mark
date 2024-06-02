# mind-mark
Creating a mind-map using markdown like syntax

## usage
Convert following text to a mind-map diagram
```
# this is comments
@style #TechResearch fill:#f9f,stroke:#333,stroke-width:4px,color:#333
@macro #M?=max(/M#(\d+)/g)

Root node
  Cards #M? &?
    @enable AutoNumber
    Epic A #M? &?
      Story A.1 #M1 &3d
      Story A.2 #M1 &2d
    Epic B #M? &?
      Story B.1 #M2 &1w
      Story B.2 #M3 &1d
  Tech Research Tasks &?
    @include Cards(#TechResearch)
  Tech Design Cards &?
    @include Cards(#TechDesign)
  Milestones
    M1 实现基本功能 &?
      @include Cards(#M1)
    M2 实现附加的功能 &?
      @include Cards(#M2)
    M3 实现更多的功能 &?
      @include Cards(#M3)
```