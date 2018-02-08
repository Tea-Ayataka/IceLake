/**
 * Transform SWF is an API for reading and writing Flash files.
 *
 * <p>
 * Flash is designed to display vector graphics across the Internet and
 * displayed on host devices using the Macromedia Flash Player. Flash is
 * designed to be compact, extensible and platform independent. Animations
 * delivered using Flash will be displayed consistently and accurately across a
 * wide range of devices.
 * </p>
 *
 * <p>
 * A Flash file (movie) contain a sequence of tagged data blocks.
 * </p>
 *
 * <img src="doc-files/taggedData.gif"/>
 *
 * <p>
 * Each movie starts with header information followed by a sequence of tags that
 * describe how the movie is displayed. All movies end with an End tag which
 * marks the end of the file.
 * </p>
 *
 * <p>
 * The header identifies the type and version of Flash contained in the file;
 * the size of screen displayed by the Flash Player; the length of file; the
 * rate at which frames are displayed and the number of frames in the movie.
 * </p>
 *
 * <p>
 * Two categories of tag are provided: <i>definition</i> tags which are used to
 * describe shapes, images, buttons, sounds, etc., and <i>control</i> tags that
 * create instances of the shapes, etc. and display them on the Flash Player
 * screen to create animations.
 * </p>
 *
 * <p>
 * Flash also supports actions which control the behaviour of objects in
 * response to events such as a button being clicked or when a particular frame
 * is displayed. Actions are represented by byte-codes and the Flash Player
 * supports a stack-based programming model which provides a rich programming
 * environment that supports complex behaviours. This allows Flash movies to
 * provide all the flexibility and sophistication of user interfaces found in
 * desktop computers and personal devices.
 * </p>
 *
 * <p>
 * The Transform SWF library is a collection of classes for each of the tags and
 * data structures that make up the Flash File Format. The Movie class is the
 * principal class in the library. Movie objects are used to represent a
 * complete Flash movie, containing instances of the classes that represent each
 * type of tag.
 * </p>
 *
 * <p>
 * Movie is designed to simplify the creation and manipulation of Flash files.
 * Creating an instance of the class:
 * </p>
 *
 * <pre>
 * Movie aMovie(filename);
 * </pre>
 *
 * <p>
 * parses the Flash file, <em>filename</em> and creates objects for each
 * different tags and data structure decoded from the file. The objects may be
 * inspected and their attributes changed accordingly. Once the objects have
 * been updated:
 * </p>
 *
 * <pre>
 * aMovie.writeToFile(filename);
 * </pre>
 *
 * <p>
 * encodes each object and generates the file according to the Macromedia Flash
 * (SWF) File Format Specification.
 * </p>
 *
 * <H1 class="datasheet">1.1. Flash Programming Model</H1>
 *
 * <p>
 * Flash is, in effect, a programming language for the Flash Player. A movie
 * contains definitions of objects which are created, displayed and updated to
 * create an animation. In order to understand how the classes provided in
 * Transform are used to create Flash movies it is essential to understand the
 * programming model provided by the Flash Player.
 * </p>
 *
 * <img src="doc-files/player.gif">
 *
 * <p>
 * The <b>Dictionary</b> is a table that contains the definitions of the objects
 * (buttons, images, sounds) that will be displayed in the movie. Each
 * definition tag (represented in Transform by classes derived from Definition)
 * contains a unique identifier which allows the definition to be referenced
 * when creating instances of a particular shape, button, etc., to display on
 * the screen. Entries are added to the dictionary whenever a definition tag is
 * decoded from the Flash movie. Dictionary entries may be deleted using the
 * Free class once a definition is no longer needed.
 * </p>
 *
 * <p>
 * The <b>Display List</b> controls the order in which objects are displayed on
 * the screen. The display list is divided into layers. To display an object on
 * the screen it is placed on a display list layer (using the PlaceObject or
 * PlaceObject2 classes). The layer number controls the stacking order of object
 * when the movie is displayed. Objects placed on a higher layer are displayed
 * in front on an object on a lower layer. Only one object can be placed on a
 * given layer at a time so like the unique identifier used to create the object
 * from its definition in the Dictionary the layer number can be used to refer
 * to an object once it is on the display list. When placed on the Display List
 * an object is always visible (unless hidden by another or moved off the
 * visible area of the screen) until it is explicitly removed (using
 * RemoveObject or RemoveObject2). Once an object is removed it is "destroyed"
 * so another instance must be re-created from its definition if the object must
 * be displayed again.
 * </p>
 *
 * <p>
 * The <b>Movie List</b> is used to manage the movie and any movie clips (also
 * known as sprites). The Movie List contains a number of virtual levels,
 * analogous to the layers in the Display List, which are used to keep track of
 * which movie clips are currently loaded. The main movie is placed
 * automatically on _level0. Movie clips that are displayed within the main
 * movie may be loaded to _level1, _level2, etc. Note that movie clips must
 * still be placed on the Display List for them to become visible on the Screen.
 * Loading a movie clip (using the GetUrl2 class) to a level replaces any clip
 * already loaded at that level. Movies loaded to _level0 replace the main
 * movie. This allows several movies sequences to be chained together into a
 * single movie.
 * </p>
 *
 * <p>
 * The <b>Screen</b> is a bit-mapped display where the Flash Player renders the
 * graphics objects to create the animation. Each time a ShowFrame tag is
 * decoded the objects added to the Display List are rendered on the Screen. All
 * graphics and fonts are anti-aliased. The size of the Screen is defined by the
 * frameSize attribute of the Movie object. Coordinates in Flash are specified
 * in <b>twips</b> where 1 twip equals 1/1440th of an inch or 1/20th of a point.
 * For most practical purposes this can be simplified to 1 twip = 1/20th of a
 * pixel.
 * </p>
 *
 * <p>
 * It is important to note that in Flash the positive x and y axes extend from
 * the top left corner of the screen (not from the lower left as in the
 * Cartesian coordinate system). The coordinate range may also be specified in
 * the attributes of a Flash movie so that the top left corner is set to an
 * arbitrary pair of coordinates. For example specifying the corners at the
 * points (-100,-100) and (100,100) defines a screen 200 twips by 200 twips with
 * the point (0,0) located in the centre. Specifying the points (0,0) and
 * (200,200) defines a screen with the same size however the centre is now
 * located at (100,100).
 * </p>
 *
 * <p>
 * The Flash Player supports three other physical devices (through the host
 * operating system) to allow a user to interact with the animation being
 * played. Users can enter data and keystrokes representing commands through a
 * keyboard. A mouse or other pointing device is supported to allow users to
 * click buttons and generate events for movie clips. Finally the Flash Player
 * supports CD-quality sound in mono or stereo with between three and eight
 * sound channels playing simultaneously, depending on the capabilities of the
 * host platform. Sounds can be played in response to specific events, such as a
 * mouse being clicked or streamed with the movie to provide as sound-track as
 * it is displayed. With the introduction of Flash 5, access to the keyboard and
 * mouse are provided through the Key and Mouse objects and the methods they
 * support, see Object-Oriented Programming below.
 * </p>
 *
 * <p>
 * Actions executed by the Flash Player are loaded into the <b>Instruction
 * Buffer</b>. Actions may be specified as part of a button definition to be
 * executed when the button is clicked. Flash also allows actions to be executed
 * whenever each frame is displayed (using the DoAction class). Flash does not
 * support the concept of a program as such. Rather the complete movie can be
 * regarded as the program executed by the Flash Player. Sequences of actions
 * defined in DefineButton, DoAction objects are best viewed as procedures or
 * sub-routines that are executed whenever a particular event occurs. A given
 * set of actions are executed each and every time the corresponding event
 * occurs. The Instruction Buffer is byte-addressable. Control flow within the
 * Instruction Buffer is controlled by the <b>Instruction Pointer</b> which
 * contains the address of the next instruction to be executed. Conditional and
 * unconditional jumps are supported through the If and Jump classes
 * respectively. Each action contains a 16-bit offset which is added to the
 * address in the Instruction Pointer to obtain the address of the next
 * instruction in the Instruction Buffer.
 * </p>
 *
 * <p>
 * Calculations are performed using the <b>Stack</b>. The Push class is used to
 * push values onto the stack. When actions are executed any required arguments
 * are popped from the Stack and any results are pushed back on the stack then
 * the action finishes executing. Values pushed onto the Stack are primarily
 * integers or strings. When values are used repeatedly in a calculation the
 * Flash Player provides the <b>String Table</b> and a set of 256 internal
 * <b>Registers</b> (increased from 4 in Flash 6) to improve efficiency. Rather
 * than repeatedly push a string value onto the Stack the Table class allows an
 * array of strings to be defined in a look-up table, using the Table class. A
 * string is referenced from the table by pushing an TableIndex value onto the
 * Stack. A table index only occupies 1 or 2 bytes in the Instruction Buffer so
 * the space required by a sequence of actions can be reduced considerably using
 * indices rather than explicitly pushing strings onto the stack. The Flash
 * Player also has a set of 256 internal registers for temporarily storing
 * values. Values are copied (not popped) from the top of the Stack using the
 * RegisterCopy action. The contents of the register is referenced by pushing an
 * RegisterIndex onto the Stack. This simplifies use of the Stack is complex
 * calculations and again reduced the space required to represent a given set of
 * actions in the Instruction Buffer.
 * </p>
 *
 * <p>
 * Although calculations are performed using the Stack, Flash also supports the
 * concept of named variables. Variables are stored in <b>Memory</b> which is a
 * scratch-pad memory accessible only by the Flash Player. It cannot be accessed
 * using any of the actions provided by Flash. Variables are fetched and set
 * from Memory using the GetVariable and SetVariable actions (byte-code actions
 * are supported using the Action class). The name of the variable is pushed
 * onto the Stack and a GetVariable action is executed. The value of the
 * variable is fetched form Memory and pushed onto the Stack by the Flash
 * Player. Similarly to set a variable the value followed by the variable name
 * are pushed onto the Stack and a SetVariable action is executed to update the
 * variable in Memory.
 * </p>
 *
 * <h1 class="datasheet">1.2. A Quick Tour of Flash</h1>
 *
 * <p>
 * Flash provides a rich environment for drawing object and creating animations.
 * This is a brief description of the main aspects of Flash and is intended to
 * introduce the main classes available in the Transform SWF library. Examples
 * showing how to use the individual classes to create Flash movies are
 * presented on Flagstone's web site.
 * </p>
 *
 * <p>
 * <b>Drawing Shapes</b><br/>
 * Shapes are drawn using a combination Line and Curve which specify the line or
 * curve to draw relative to the current drawing point. The line style is
 * defined using SolidLine class - only solid lines are supported. Shapes can be
 * filled with solid colour (SolidFill), an image (BitmapFill) or a colour
 * gradient (GradientFill). The ShapeStyle class is used to select a particular
 * line and fill style while drawing a shape. It can also change the current
 * drawing point and the set of line and fill styles used for a particular
 * shape. Shapes are defined using DefineShape, DefineShape2 or DefineShape3.
 * DefineShape is the most commonly used class. DefineShape2 extends DefineShape
 * to support a large number (greater than 65535) of line and fill styles while
 * DefineShape3 supports transparent shapes.
 * </p>
 *
 * <p>
 * Flash supports shape morphing using the DefineMorphShape class which defines
 * the appearance of the start and end shapes. The interpolation between the
 * start and end shapes is performed by the Flash Player. Displaying a morphing
 * shape is controlled by the PlaceObject2 class which identifies the point in
 * the morphing process and updates the display list with the morphed shape.
 * </p>
 *
 * <p>
 * <b>Buttons</b><br/>
 * Buttons are created by defining a shape for when button is up, the mouse is
 * over the button or the button is clicked.DefineButton displays a button that
 * executes a set of actions when it is clicked. DefineButton2 provides are more
 * flexible class allowing actions to be executed for any of the states a button
 * can occupy. Sounds can also be played, using the ButtonSound class, when an
 * event occurs, such as the button being clicked.
 * </p>
 *
 * <p>
 * <b>Images</b><br/>
 * Flash supports JPEG and lossless image formats such as GIF. Several classes
 * are provided which display "regular" and transparent images. Images cannot be
 * displayed directly. They can only be displayed inside a shape that contains a
 * bitmap fill style. Creating such "image shapes" is simple however.
 * </p>
 *
 * <p>
 * <b>Fonts and Text</b><br/>
 * Flash ensures consistency and quality when display text by defining the
 * glyphs that represent each of the text characters displayed in a movie. Fonts
 * are defined using DefineFont and DefineFont2 - the latter combines the
 * DefineFont and FontInfo classes into a more convenient and flexible class. A
 * string is defined, using DefineText and Text, by specifying the index of the
 * glyph, from the font definition, that represents a given character. Using the
 * index of a glyph allows only the glyphs actually displayed to be used,
 * greatly reducing the size of a Flash file. The relative placement of glyphs
 * is also specified in the text string giving a high degree of control over how
 * a piece of text is rendered.
 * </p>
 *
 * <p>
 * Creating fonts and text directly using the Transform classes can be very
 * cumbersome. Flagstone provides the <b>Transform Utilities</b> library that
 * allows fonts and text to be created from existing Flash or Java font
 * definitions greatly simplifying the process.
 * </p>
 *
 * <p>
 * Text fields can be created using the DefineTextField class. This class is
 * very flexible and can be used to create editable text fields that allow users
 * to submit information to a server in the same way as HTML forms.
 * </p>
 *
 * <p>
 * <b>Sound</b><br/>
 * Flash can supports sounds that are played when an event occurs such as a
 * button being clicked (DefineSound) or streaming sound that allows a
 * soundtrack to be played while displaying a movie (SoundStreamHead,
 * SoundStreamBlock). Flash can play sounds that are compressed using ADPCM or
 * MP3. A high degree of control over the sound is available - an envelope can
 * be created that controls how a sound fades in and fades out (Sound).
 * </p>
 *
 * <p>
 * <b>Movie Clips</b><br/>
 * Movie Clips (sprites) contain "pre-packaged" animation sequences that liven
 * up a movie. Movie clip are defined using the DefineMovie class. A Flash file
 * can contain all the required movie clips or they can be loaded on demand
 * using the GetUrl and GetUrl2 classes.
 * </p>
 *
 * <p>
 * <b>Display List</b><br/>
 * Shapes, buttons, images and text are placed on the display list using the
 * PlaceObject and PlaceObject2 classes. Objects can be removed from the display
 * list using RemoveObject and RemoveObject2.
 * </p>
 *
 * <p>
 * <b>Timelines</b><br/>
 * A Flash file is composed of a series of frames. Each frame contains
 * definitions of objects to be displayed and the sequence of commands used to
 * update the display list. Frames in a movie are displayed using the ShowFrame
 * class. This instructs the Flash Player to display all the objects currently
 * in the display list. Successive frames in a movie or movie clip are delimited
 * by successive ShowFrame objects. The timeline is controlled by GotoFrame,
 * GotoFrame2 and GotoLabel actions which instruct the Flash Player to play the
 * movie or clip starting at a particular frame.
 * </p>
 *
 * <p>
 * <b>Actions</b><br/>
 * Actions are executed when a frame is displayed, a button is clicked or an
 * event occurs in a movie clip. The DoAction class is used to define the list
 * of actions for a given frame. The ButtonEvent class defines actions for a
 * button and the ClipEvent class in conjunction with the PlaceObject2 class is
 * used to define the events for a movie clip.
 * </p>
 *
 * <p>
 * Push is used to push integers, strings, double precision floating point
 * numbers onto the Stack. The byte-codes that represent stack-based actions are
 * supported using the Action class. The type assigned to instances of this
 * class identify the type of action to be executed.
 * </p>
 *
 * <p>
 * Control flow is performed using If and Jump.
 * </p>
 *
 * <p>
 * GetUrl and GetUrl2 are used to load web pages, movies and movie clips.
 * GetUrl2 also allows variables to be loaded and submitted to and from a remote
 * server. XML formatted data can also be exchanged using the XMLSocket class
 * allowing direct communication with a server process.
 * </p>
 *
 * <h1 class="datasheet">1.3. Object-Oriented Programming</h1>
 *
 * <p>
 * With the introduction of Flash 5 the concept of classes and objects (in the
 * programming sense) was introduced to Flash. The Flash Player supports several
 * pre-defined classes for different data types such as Strings, Integers and
 * XML formatted data structures. Classes are also used access to system
 * resources such as the mouse or other pointing device supported by the host
 * device. The Flash Player also supports <b>Socket</b> objects which allow XML
 * formatted data to be exchanged between the Flash Player and a remote server.
 * </p>
 *
 * <p>
 * Objects are instantiated and assigned to variables in Memory. Instances of
 * pre-defined classes (String, Integer, etc.) are created by pushing any
 * arguments followed by the name of the class onto the Stack then executing a
 * NamedObject action. Methods are executed by pushing the arguments, if any,
 * followed by a reference to the object (usually through a variable) and a
 * string containing the name of a method. The ExecuteMethod pops the items from
 * the stack and executes the function associated with the method definition,
 * returning any results to the stack.
 * </p>
 *
 * <p>
 * User defined classes and objects can also be defined and created.
 * User-defined classes are created by pushing pairs of strings and default
 * values onto the Stack for each member variable then executing the NewObject
 * action. A reference to the object in memory is pushed on the Stack. Assigning
 * this reference to a variable allows the object to be referenced in future
 * calculations. Methods can be defined by using the NewFunction class to define
 * a function and then assigning it to a member variable of the class using the
 * SetAttribute action. Methods can be executed using the ExecuteMethod action
 * using the same sequence of operations described previously.
 * </p>
 *
 * <p>
 * The introduction of classes and objects is significant. Future releases of
 * Flash and the Flash Player can have new functionality added using the general
 * purpose mechanism describe above for creating objects and executing methods
 * without adding new tags to the file format specification.
 * </p>
 *
 * <h1 class="datasheet">1.4. ActionScript</h1>
 *
 * <p>
 * ActionScript is a high-level language provided by Macromedia to control and
 * manipulate objects using the Flash authoring program. ActionScript is
 * compiled into the byte-codes and actions supported by Transform when a movie
 * is encoded to the Flash file format. Programming directly using the actions
 * defined in Transform is analogous to assembly-language programming for
 * microprocessors. Creating programs at this low level is relatively
 * cumbersome, prone to error and difficult to debug. To address this, Flagstone
 * developed <b>Translate</b>, a Java based program that compiles ActionScript
 * into the objects supported in the Transform SWF Library. This greatly
 * simplifies the creation of programs that can be used in Flash files. Although
 * Translate is current only available for Java, compiled scripts can easily be
 * added to programs developed in C++ by compiling the ActionScript and encoding
 * the objects to a Flash file. The file can then be parsed using the Movie
 * class and the compiled actions extracted and added to the relevant objects in
 * the current movie.
 * </p>
 */
package com.flagstone.transform;
