package exampleSupport;



import java.awt.*;
import java.lang.reflect.*;
import java.io.*;



/**
 * This class contain small but useful functions.  This class is not meant to be
 * subclassed.
 *
 * @author  Daniel E. Barbalace
 */

public final class Misc
{



/** Frame used to create dummyWindow */
private static Frame dummyFrame = null;

/** Window used to create images */
private static Window dummyWindow = null;

/** Offset to the calling method in a stack trace */
private static int levelOffset = calculateLevelOffset();



/**
 * Determines if a string represents an IP address.
 *
 * @param string    String being examined
 *
 * @return True, if the string represents an IP address.  False, otherwise.
 */

public static final boolean isIPAddress (String string)
{
    int     currentState = 0; // Current state of finite state machine
    int     currentDigit = 1; // Current digit in a 3 byte number
    int     countChar;        // Counts characters
    char    text[];           // Array representation of string
    boolean valid = true;     // Is the string a valid IP address
    int     lastState;        // State in when digit was examined
    int     countDigit;       // Counts digits in the current number
    int     number;           // Value of a single number in an IP address
    int     value;            // Value of a digit based on its place

    text = string.toCharArray();

    for (countChar = 0; countChar < text.length; countChar++)
    {
        switch (currentState)
        {
            // States expecting a 1 to 3 digit number
            case 1 :
            case 3 :
            case 5 :
            case 7 :
                // Remember the current state and the current digit
                lastState = currentState;

                if (Character.isDigit(text[countChar]))
                {
                    // Character is a digit, increment count
                    currentDigit++;

                    // Only three digits are allowed in a number
                    if (currentDigit == 4)
                        currentState++;
                }
                else
                {
                    // Make sure at least one digit was specified
                    if (currentDigit == 1)
                        valid = false;
                    else
                    {
                        // A non-digit forces the fsm to the next state
                        currentState++;
                        countChar--;
                    }
                }

                // Has the state has changed
                if ((currentState != lastState) ||
                    (countChar == text.length - 1))
                {
                    // Initialize number and exponent
                    number = 0;
                    value = (int) Math.pow(10, currentDigit - 2);

                    // Convert text to number
                    for (countDigit = countChar - currentDigit + 2; 
                         countDigit <= countChar; countDigit++)
                    {
                        // Add value of digit to number
                        number += Character.digit(text[countDigit], 10) * value;
                        value /= 10;
                    }

                    // Check that the number is between 0 and 255
                    if (number > 255)
                        valid = false;
                }
            break;

            // States expecting a single period
            case 2 :
            case 4 :
            case 6 :
                if (text[countChar] == '.')
                {
                    currentDigit = 1;
                    currentState++;
                }
                else
                    valid = false;
            break;

            // States expecting a whitespace
            case 0 :
            case 8 :
                // Space and tab (ASCII 9) are the only supported whitespaces
                if ((text[countChar] != ' ') && (text[countChar] != 0x09))
                {
                    currentState++;
                    countChar--;
                }
            break;
        }

        // Do not need to continue if string is not a valid IP address
        if (!valid)
            break;
    }

    // Valid flag must be set and the final state must be 7 or 8
    return valid && ((currentState == 7) || (currentState == 8));
}



/**
 * Scales up or down a dimension, component, so that it fits inside another
 * dimension, container, while preserving the aspect ratio of component.
 *
 * @param container     Containing dimension
 * @param component     Contained dimension
 *
 * @return the largest dimension that has the aspect ratio of component and is
 *         no larger than container
 */

public static final Dimension scaleUpOrDown
    (Dimension container, Dimension component)
{
    int width;  // Width of returned dimension
    int height; // Height of returned dimension

    // Get width and height ratios
    double widthRatio = ((double) container.width) / component.width;
    double heightRatio = ((double) container.height) / component.height;

    // Use whichever ratio is smaller -- scaled down to keep aspect ratio
    // Anything that uses the returned dimension will be padded, not cropped
    if (widthRatio < heightRatio)
    {
        width = (int) (component.width * widthRatio);
        height = (int) (component.height * widthRatio);
    }
    else
    {
        width = (int) (component.width * heightRatio);
        height = (int) (component.height * heightRatio);
    }

    // Create the scaled dimension
    return new Dimension(width, height);
}



/**
 * Scales down a dimension, component, so that it fits inside another dimension,
 * container, while preserving the aspect ratio of component.
 *
 * @param container     Containing dimension
 * @param component     Contained dimension
 *
 * @return the largest dimension that has the aspect ratio of component and is
 *         no larger than either component or container
 */

public static final Dimension scaleDown
    (Dimension container, Dimension component)
{
    Dimension scaled = scaleUpOrDown(container, component);

    if ((scaled.width > component.width) || (scaled.height > component.height))
        // Dimension was scaled up, use the original since it is small enough
        // to fit inside the container
        return component;
    else
        // Dimension was scaled down, use the new dimension
        return scaled;
}



/**
 * Converts a string to an integer.
 *
 * @param string    integer represented as a string
 *
 * @return If <code>string</code> represents a valid number, the number
 *         represented.  Otherwise, 0.
 */

public static final int integer (String string)
{
    int value = 0;

    try
    {
        value = new Integer(string).intValue();
    }
    catch (NumberFormatException error) {}

    return value;
}



/**
 * Creates an image of the given size.  Unlike <code>component.createImage
 * </code>, this method will not return null.
 *
 * @param dimension    size of the image
 *
 * @return a <code>dimension.width</code> by <code>dimension.height</code> image
 */

public static final Image createImage (Dimension dimension)
{
    return createImage(dimension.width, dimension.height);
}



/**
 * Creates an image of the given width and height.  Unlike <code>
 * component.createImage</code>, this method will not return null.
 *
 * @param width     width of the image
 * @param height    height of the image
 *
 * @return a <code>width</code> by <code>height</code> image
 */

public static final Image createImage (int width, int height)
{
    // Check parameters
    if (width < 1)
        width = 1;

    if (height < 1)
        height = 1;

    // Make sure frame exists
    if (dummyWindow == null)
    {
        dummyFrame = new Frame();
        dummyWindow = new Window(dummyFrame);
        dummyWindow.setSize (0, 0);
        dummyWindow.setVisible(true);
        dummyWindow.toFront();
    }

    // Use the window to create the image (since it has a peer)
    return dummyWindow.createImage(width, height);
}



/**
 * Gets the topmost container of a given component.
 *
 * @param component    component being examined
 *
 * @return a container that directly or indirectly contains <code>component
 *         </code>, but is not contained by anything, or null is component does
 *         not have a parent (container)
 */

public static final Container getTopmostContainer (Component component)
{
    // Make sure component is not null
    if (component == null)
        return null;

    // Get parent
    Container next = component.getParent();

    // Continue up the ancestor chain
    while (next != null)
        next = next.getParent();

    return next;
}



/**
 * Packs an array into it's most condense form.
 *
 * @param array    any array of objects
 *
 * @return If <code>array</code> is null, an array of zero objects.  If
 *         <code>array</code> is not null, an array of n objects where n is
 *         the number of non-null elements in <code>array</code>.
 *
 *         <p>The return value may be typed casted into the class of <code>
 *         array</code> as long as <code>array</code> was not null.  If <code>
 *         array</code> is null, it may be typed casted into an array of
 *         <code>defaultClass</code>.  If both <code>array</code> and <code>
 *         defaultClass</code> are null, the return value will be an array of
 *         class Object.</p>
 *
 *         <p>This method will never return null.</p>
 */

public static final Object [] packArray (Object array[], Class defaultClass)
{
    try
    {
        // Check for null array
        if (array == null)
            if (defaultClass == null)
                return new Object[0];
            else
                return (Object []) Array.newInstance(defaultClass, 0);

        // Get the number of non-null elements in array
        int numElement = 0;

        for (int counter = 0; counter < array.length; counter++)
            if (array[counter] != null)
                numElement++;

        // Get the array's class
        Class c = array.getClass().getComponentType();

        // Create an array that has the same type as the array parameter
        Object result[] = (Object []) Array.newInstance(c, numElement);

        // Populate the return code array
        numElement = 0;

        for (int counter = 0; counter < array.length; counter++)
            if (array[counter] != null)
                result[numElement++] = array[counter];

        return result;
    }
    catch (NegativeArraySizeException e)
    {
        // This cannot happen since numElement >= 0
        return null;
    }
}



/**
 * Gets a method.
 *
 * @param className     fully qualified name of class containing method, e.g.,
 *                      "myPackage.subPackage.myClass"
 * @param methodName    name of the method that compares two objects
 * @param paramType     the fully qualified names of the classes of every
 *                      parameter in the parameter order.
 *
 * @return On success, an instance of Method that may be used to invoke the
 *         specified method.  On failure, null.  This method will only fail if
 *         the given method cannot be found or is not accessible.  On failure,
 *         this method will print a stack trace of the exception encountered.
 */

public static final Method getMethod
    (String className, String methodName, String paramType[])
{
    try
    {
        // Make sure parameter array is not null
        if (paramType == null)
            paramType = new String[0];

        // Get class class
        Class aClass = Class.forName(className);
        
        // Get each parameter
        Class parameter[] = new Class[paramType.length];
        
        for (int counter = 0; counter < parameter.length; counter++)
            parameter[counter] = Class.forName(paramType[counter]);

        // Get method
        Method method = aClass.getMethod(methodName, parameter);

        return method;
    }
    catch (Exception error)
    {
        error.printStackTrace();
        return null;
    }
}



/**
 * Gets the name of the class and the method that called this method.
 *
 * @param level    number of stack entries to skip.  If the caller wants to know
 *                 who it is, <code>level</code> should be 1.  If the caller
 *                 wants to know who called it, <code>level</code> should be 2.
 *                 Etc.  If <code>level</code> is 0 or less, this class and
 *                 method is returned.  If <code>level</code> is too high, empty
 *                 strings are returned in a non-null array.
 *
 * @return an array, s, of three strings such that s[0] is the class name, s[1]
 *         is the method name, and s[2] is the line number.  Any, some, or all
 *         of these strings can be "", if the information could not be
 *         determined.  The array s, itself, will not be null.  s.length will
 *         always be 3.
 */

public static final String[] getClassAndMethod (int level)
{
    // Mark className and methodName as unknown
    String className = "";
    String methodName = "";
    String lineNumber = "";

    try
    {
        throw new Exception();
    }
    catch (Exception exception)
    {
        try
        {
            // Put stack trace into an array of bytes.  The array only has to be
            // large enough to store the top four lines of text.  The byte array
            // will grow to accommodate the stack trace, but it's more effecient
            // to initially allocate enough space for most stack traces.
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            PrintWriter pw = new PrintWriter(os);
            exception.printStackTrace(pw);
            pw.flush();
            byte array[] = os.toByteArray();

            // Go to start of (level) line.  The first line says stack trace.
            // Then there may be several lines before this method.
            int indexLineStart = 0;

            for (int i = 0; i < level + levelOffset; i++)
            {
                while ((array[indexLineStart] != '\n'))
                    indexLineStart++;

                indexLineStart++;
            }

            // Go to start of class name (after "at ")
            int indexClassStart = indexLineStart;

            while ((array[indexClassStart] == ' '))
                indexClassStart++;

            while ((array[indexClassStart] != ' '))
                indexClassStart++;

            indexClassStart++;

            // Go to end of the method name (right before " " or "(")
            int indexMethodEnd = indexClassStart;

            while ((array[indexMethodEnd] != ' ') &&
                   (array[indexMethodEnd] != '('))
                indexMethodEnd++;

            // Go to start of the method name (right after ".")
            int indexMethodStart = indexMethodEnd;

            while ((array[indexMethodStart] != '.'))
                indexMethodStart--;

            indexMethodStart++;

            // Go to end of class name (two characters left of method start)
            int indexClassEnd = indexMethodStart - 1;

            // Go to end of line number (two characters left of newline if
            // available in stack trace)
            int indexNumEnd = indexMethodEnd;

            while ((array[indexNumEnd] != '\n'))
                indexNumEnd++;

            indexNumEnd -= 2;

            // Go to start of line number (right after ':')
            int indexNumStart = indexNumEnd;

            while ((indexNumStart > indexMethodEnd) &&
                   (array[indexNumStart] != ':'))
                indexNumStart--;

            if (indexNumStart == indexMethodEnd)
                indexNumStart = indexNumEnd;
            else
                indexNumStart++;

            // Extract class name, method name, and line number
            className = new String
                (array, indexClassStart, indexClassEnd - indexClassStart);
            methodName = new String
                (array, indexMethodStart, indexMethodEnd - indexMethodStart);
            lineNumber = new String
                (array, indexNumStart, indexNumEnd - indexNumStart);
        }
        catch (Exception error)
        {
            // Stack trace was too big or not properly formatted.  In either
            // case, both the class and the method names are unknown.
        }
    }

    // Package strings into a single array
    String array[] = {className, methodName, lineNumber};
    return array;
}



/**
 * Calculates the number of lines to skip to find the top level of the stack
 * trace.
 */

protected static final int calculateLevelOffset ()
{
    int levelOffset = 0;

    try
    {
        throw new Exception();
    }
    catch (Exception exception)
    {
        try
        {
            // Put stack trace into an array of bytes.  The array only has to be
            // large enough to store the top four lines of text.  The byte array
            // will grow to accommodate the stack trace, but it's more effecient
            // to initially allocate enough space for most stack traces.
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            PrintWriter pw = new PrintWriter(os);
            exception.printStackTrace(pw);
            pw.flush();
            byte array[] = os.toByteArray();

            // Get the name of this class
            String nameStr = (new Misc()).getClass().getName();
            byte name[] = nameStr.getBytes();

            // Find offset to classname in terms of lines of text
            int i = 0;
            boolean found = false;

            while (!found)
            {
                // Find match for class name
                int c;

                for (c = 0; c < name.length; c++)
                    if (name[c] != array[i + c])
                        break;

                // Was a match found
                if (c == name.length)
                    found = true;
                else
                {
                    // Increment line count if a newline was reached
                    if (array[i] == '\n')
                        levelOffset++;

                    // Go to next character
                    i++;
                }
            }
        }
        catch (Exception error)
        {
            // Stack trace was too big or not properly formatted.  In either
            // case, the only thing to do is hope that the levelOffset is 0
            levelOffset = 0;
        }
    }

    return levelOffset;
}



}
