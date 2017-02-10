declare namespace java.awt {
    class Color {
        r: number;
        g: number;
        b: number;
        constructor(r: number, g: number, b: number);
        toHTML(): string;
    }
}
declare namespace java.awt {
    class Component {
    }
}
declare namespace java.awt.event {
    class ActionEvent {
        private source;
        private actionCommand;
        constructor(source: any, actionCommand: string);
        getSource(): any;
        getActionCommand(): string;
    }
}
declare namespace java.awt.event {
    interface ActionListener {
        actionPerformed(ae: java.awt.event.ActionEvent): any;
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>AffineTransform</code> class represents a 2D affine transform
     * that performs a linear mapping from 2D coordinates to other 2D
     * coordinates that preserves the "straightness" and
     * "parallelness" of lines.  Affine transformations can be constructed
     * using sequences of translations, scales, flips, rotations, and shears.
     * <p>
     * Such a coordinate transformation can be represented by a 3 row by
     * 3 column matrix with an implied last row of [ 0 0 1 ].  This matrix
     * transforms source coordinates {@code (x,y)} into
     * destination coordinates {@code (x',y')} by considering
     * them to be a column vector and multiplying the coordinate vector
     * by the matrix according to the following process:
     * <pre>
     * [ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
     * [ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
     * [ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
     * </pre>
     * <h3><a name="quadrantapproximation">Handling 90-Degree Rotations</a></h3>
     * <p>
     * In some variations of the <code>rotate</code> methods in the
     * <code>AffineTransform</code> class, a double-precision argument
     * specifies the angle of rotation in radians.
     * These methods have special handling for rotations of approximately
     * 90 degrees (including multiples such as 180, 270, and 360 degrees),
     * so that the common case of quadrant rotation is handled more
     * efficiently.
     * This special handling can cause angles very close to multiples of
     * 90 degrees to be treated as if they were exact multiples of
     * 90 degrees.
     * For small multiples of 90 degrees the range of angles treated
     * as a quadrant rotation is approximately 0.00000121 degrees wide.
     * This section explains why such special care is needed and how
     * it is implemented.
     * <p>
     * Since 90 degrees is represented as <code>PI/2</code> in radians,
     * and since PI is a transcendental (and therefore irrational) number,
     * it is not possible to exactly represent a multiple of 90 degrees as
     * an exact double precision value measured in radians.
     * As a result it is theoretically impossible to describe quadrant
     * rotations (90, 180, 270 or 360 degrees) using these values.
     * Double precision floating point values can get very close to
     * non-zero multiples of <code>PI/2</code> but never close enough
     * for the sine or cosine to be exactly 0.0, 1.0 or -1.0.
     * The implementations of <code>Math.sin()</code> and
     * <code>Math.cos()</code> correspondingly never return 0.0
     * for any case other than <code>Math.sin(0.0)</code>.
     * These same implementations do, however, return exactly 1.0 and
     * -1.0 for some range of numbers around each multiple of 90
     * degrees since the correct answer is so close to 1.0 or -1.0 that
     * the double precision significand cannot represent the difference
     * as accurately as it can for numbers that are near 0.0.
     * <p>
     * The net result of these issues is that if the
     * <code>Math.sin()</code> and <code>Math.cos()</code> methods
     * are used to directly generate the values for the matrix modifications
     * during these radian-based rotation operations then the resulting
     * transform is never strictly classifiable as a quadrant rotation
     * even for a simple case like <code>rotate(Math.PI/2.0)</code>,
     * due to minor variations in the matrix caused by the non-0.0 values
     * obtained for the sine and cosine.
     * If these transforms are not classified as quadrant rotations then
     * subsequent code which attempts to optimize further operations based
     * upon the type of the transform will be relegated to its most general
     * implementation.
     * <p>
     * Because quadrant rotations are fairly common,
     * this class should handle these cases reasonably quickly, both in
     * applying the rotations to the transform and in applying the resulting
     * transform to the coordinates.
     * To facilitate this optimal handling, the methods which take an angle
     * of rotation measured in radians attempt to detect angles that are
     * intended to be quadrant rotations and treat them as such.
     * These methods therefore treat an angle <em>theta</em> as a quadrant
     * rotation if either <code>Math.sin(<em>theta</em>)</code> or
     * <code>Math.cos(<em>theta</em>)</code> returns exactly 1.0 or -1.0.
     * As a rule of thumb, this property holds true for a range of
     * approximately 0.0000000211 radians (or 0.00000121 degrees) around
     * small multiples of <code>Math.PI/2.0</code>.
     *
     * @author Jim Graham
     * @since 1.2
     */
    class AffineTransform implements java.lang.Cloneable, java.io.Serializable {
        static TYPE_UNKNOWN: number;
        /**
         * This constant indicates that the transform defined by this object
         * is an identity transform.
         * An identity transform is one in which the output coordinates are
         * always the same as the input coordinates.
         * If this transform is anything other than the identity transform,
         * the type will either be the constant GENERAL_TRANSFORM or a
         * combination of the appropriate flag bits for the various coordinate
         * conversions that this transform performs.
         * @see #TYPE_TRANSLATION
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_FLIP
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @see #getType
         * @since 1.2
         */
        static TYPE_IDENTITY: number;
        /**
         * This flag bit indicates that the transform defined by this object
         * performs a translation in addition to the conversions indicated
         * by other flag bits.
         * A translation moves the coordinates by a constant amount in x
         * and y without changing the length or angle of vectors.
         * @see #TYPE_IDENTITY
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_FLIP
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @see #getType
         * @since 1.2
         */
        static TYPE_TRANSLATION: number;
        /**
         * This flag bit indicates that the transform defined by this object
         * performs a uniform scale in addition to the conversions indicated
         * by other flag bits.
         * A uniform scale multiplies the length of vectors by the same amount
         * in both the x and y directions without changing the angle between
         * vectors.
         * This flag bit is mutually exclusive with the TYPE_GENERAL_SCALE flag.
         * @see #TYPE_IDENTITY
         * @see #TYPE_TRANSLATION
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_FLIP
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @see #getType
         * @since 1.2
         */
        static TYPE_UNIFORM_SCALE: number;
        /**
         * This flag bit indicates that the transform defined by this object
         * performs a general scale in addition to the conversions indicated
         * by other flag bits.
         * A general scale multiplies the length of vectors by different
         * amounts in the x and y directions without changing the angle
         * between perpendicular vectors.
         * This flag bit is mutually exclusive with the TYPE_UNIFORM_SCALE flag.
         * @see #TYPE_IDENTITY
         * @see #TYPE_TRANSLATION
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_FLIP
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @see #getType
         * @since 1.2
         */
        static TYPE_GENERAL_SCALE: number;
        /**
         * This constant is a bit mask for any of the scale flag bits.
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @since 1.2
         */
        static TYPE_MASK_SCALE: number;
        static TYPE_MASK_SCALE_$LI$(): number;
        /**
         * This flag bit indicates that the transform defined by this object
         * performs a mirror image flip about some axis which changes the
         * normally right handed coordinate system into a left handed
         * system in addition to the conversions indicated by other flag bits.
         * A right handed coordinate system is one where the positive X
         * axis rotates counterclockwise to overlay the positive Y axis
         * similar to the direction that the fingers on your right hand
         * curl when you stare end on at your thumb.
         * A left handed coordinate system is one where the positive X
         * axis rotates clockwise to overlay the positive Y axis similar
         * to the direction that the fingers on your left hand curl.
         * There is no mathematical way to determine the angle of the
         * original flipping or mirroring transformation since all angles
         * of flip are identical given an appropriate adjusting rotation.
         * @see #TYPE_IDENTITY
         * @see #TYPE_TRANSLATION
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @see #getType
         * @since 1.2
         */
        static TYPE_FLIP: number;
        /**
         * This flag bit indicates that the transform defined by this object
         * performs a quadrant rotation by some multiple of 90 degrees in
         * addition to the conversions indicated by other flag bits.
         * A rotation changes the angles of vectors by the same amount
         * regardless of the original direction of the vector and without
         * changing the length of the vector.
         * This flag bit is mutually exclusive with the TYPE_GENERAL_ROTATION flag.
         * @see #TYPE_IDENTITY
         * @see #TYPE_TRANSLATION
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_FLIP
         * @see #TYPE_GENERAL_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @see #getType
         * @since 1.2
         */
        static TYPE_QUADRANT_ROTATION: number;
        /**
         * This flag bit indicates that the transform defined by this object
         * performs a rotation by an arbitrary angle in addition to the
         * conversions indicated by other flag bits.
         * A rotation changes the angles of vectors by the same amount
         * regardless of the original direction of the vector and without
         * changing the length of the vector.
         * This flag bit is mutually exclusive with the
         * TYPE_QUADRANT_ROTATION flag.
         * @see #TYPE_IDENTITY
         * @see #TYPE_TRANSLATION
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_FLIP
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @see #getType
         * @since 1.2
         */
        static TYPE_GENERAL_ROTATION: number;
        /**
         * This constant is a bit mask for any of the rotation flag bits.
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @since 1.2
         */
        static TYPE_MASK_ROTATION: number;
        static TYPE_MASK_ROTATION_$LI$(): number;
        /**
         * This constant indicates that the transform defined by this object
         * performs an arbitrary conversion of the input coordinates.
         * If this transform can be classified by any of the above constants,
         * the type will either be the constant TYPE_IDENTITY or a
         * combination of the appropriate flag bits for the various coordinate
         * conversions that this transform performs.
         * @see #TYPE_IDENTITY
         * @see #TYPE_TRANSLATION
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_FLIP
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @see #getType
         * @since 1.2
         */
        static TYPE_GENERAL_TRANSFORM: number;
        /**
         * This constant is used for the internal state variable to indicate
         * that no calculations need to be performed and that the source
         * coordinates only need to be copied to their destinations to
         * complete the transformation equation of this transform.
         * @see #APPLY_TRANSLATE
         * @see #APPLY_SCALE
         * @see #APPLY_SHEAR
         * @see #state
         */
        static APPLY_IDENTITY: number;
        /**
         * This constant is used for the internal state variable to indicate
         * that the translation components of the matrix (m02 and m12) need
         * to be added to complete the transformation equation of this transform.
         * @see #APPLY_IDENTITY
         * @see #APPLY_SCALE
         * @see #APPLY_SHEAR
         * @see #state
         */
        static APPLY_TRANSLATE: number;
        /**
         * This constant is used for the internal state variable to indicate
         * that the scaling components of the matrix (m00 and m11) need
         * to be factored in to complete the transformation equation of
         * this transform.  If the APPLY_SHEAR bit is also set then it
         * indicates that the scaling components are not both 0.0.  If the
         * APPLY_SHEAR bit is not also set then it indicates that the
         * scaling components are not both 1.0.  If neither the APPLY_SHEAR
         * nor the APPLY_SCALE bits are set then the scaling components
         * are both 1.0, which means that the x and y components contribute
         * to the transformed coordinate, but they are not multiplied by
         * any scaling factor.
         * @see #APPLY_IDENTITY
         * @see #APPLY_TRANSLATE
         * @see #APPLY_SHEAR
         * @see #state
         */
        static APPLY_SCALE: number;
        /**
         * This constant is used for the internal state variable to indicate
         * that the shearing components of the matrix (m01 and m10) need
         * to be factored in to complete the transformation equation of this
         * transform.  The presence of this bit in the state variable changes
         * the interpretation of the APPLY_SCALE bit as indicated in its
         * documentation.
         * @see #APPLY_IDENTITY
         * @see #APPLY_TRANSLATE
         * @see #APPLY_SCALE
         * @see #state
         */
        static APPLY_SHEAR: number;
        static HI_SHIFT: number;
        static HI_IDENTITY: number;
        static HI_IDENTITY_$LI$(): number;
        static HI_TRANSLATE: number;
        static HI_TRANSLATE_$LI$(): number;
        static HI_SCALE: number;
        static HI_SCALE_$LI$(): number;
        static HI_SHEAR: number;
        static HI_SHEAR_$LI$(): number;
        /**
         * The X coordinate scaling element of the 3x3
         * affine transformation matrix.
         *
         * @serial
         */
        m00: number;
        /**
         * The Y coordinate shearing element of the 3x3
         * affine transformation matrix.
         *
         * @serial
         */
        m10: number;
        /**
         * The X coordinate shearing element of the 3x3
         * affine transformation matrix.
         *
         * @serial
         */
        m01: number;
        /**
         * The Y coordinate scaling element of the 3x3
         * affine transformation matrix.
         *
         * @serial
         */
        m11: number;
        /**
         * The X coordinate of the translation element of the
         * 3x3 affine transformation matrix.
         *
         * @serial
         */
        m02: number;
        /**
         * The Y coordinate of the translation element of the
         * 3x3 affine transformation matrix.
         *
         * @serial
         */
        m12: number;
        /**
         * This field keeps track of which components of the matrix need to
         * be applied when performing a transformation.
         * @see #APPLY_IDENTITY
         * @see #APPLY_TRANSLATE
         * @see #APPLY_SCALE
         * @see #APPLY_SHEAR
         */
        state: number;
        /**
         * This field caches the current transformation type of the matrix.
         * @see #TYPE_IDENTITY
         * @see #TYPE_TRANSLATION
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_FLIP
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @see #TYPE_UNKNOWN
         * @see #getType
         */
        private type;
        constructor(m00?: any, m10?: any, m01?: any, m11?: any, m02?: any, m12?: any, state?: any);
        /**
         * Returns a transform representing a translation transformation.
         * The matrix representing the returned transform is:
         * <pre>
         * [   1    0    tx  ]
         * [   0    1    ty  ]
         * [   0    0    1   ]
         * </pre>
         * @param tx the distance by which coordinates are translated in the
         * X axis direction
         * @param ty the distance by which coordinates are translated in the
         * Y axis direction
         * @return an <code>AffineTransform</code> object that represents a
         * translation transformation, created with the specified vector.
         * @since 1.2
         */
        static getTranslateInstance(tx: number, ty: number): AffineTransform;
        /**
         * Returns a transform representing a rotation transformation.
         * The matrix representing the returned transform is:
         * <pre>
         * [   cos(theta)    -sin(theta)    0   ]
         * [   sin(theta)     cos(theta)    0   ]
         * [       0              0         1   ]
         * </pre>
         * Rotating by a positive angle theta rotates points on the positive
         * X axis toward the positive Y axis.
         * Note also the discussion of
         * <a href="#quadrantapproximation">Handling 90-Degree Rotations</a>
         * above.
         * @param theta the angle of rotation measured in radians
         * @return an <code>AffineTransform</code> object that is a rotation
         * transformation, created with the specified angle of rotation.
         * @since 1.2
         */
        static getRotateInstance$double(theta: number): AffineTransform;
        /**
         * Returns a transform that rotates coordinates around an anchor point.
         * This operation is equivalent to translating the coordinates so
         * that the anchor point is at the origin (S1), then rotating them
         * about the new origin (S2), and finally translating so that the
         * intermediate origin is restored to the coordinates of the original
         * anchor point (S3).
         * <p>
         * This operation is equivalent to the following sequence of calls:
         * <pre>
         * AffineTransform Tx = new AffineTransform();
         * Tx.translate(anchorx, anchory);    // S3: final translation
         * Tx.rotate(theta);                  // S2: rotate around anchor
         * Tx.translate(-anchorx, -anchory);  // S1: translate anchor to origin
         * </pre>
         * The matrix representing the returned transform is:
         * <pre>
         * [   cos(theta)    -sin(theta)    x-x*cos+y*sin  ]
         * [   sin(theta)     cos(theta)    y-x*sin-y*cos  ]
         * [       0              0               1        ]
         * </pre>
         * Rotating by a positive angle theta rotates points on the positive
         * X axis toward the positive Y axis.
         * Note also the discussion of
         * <a href="#quadrantapproximation">Handling 90-Degree Rotations</a>
         * above.
         *
         * @param theta the angle of rotation measured in radians
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @return an <code>AffineTransform</code> object that rotates
         * coordinates around the specified point by the specified angle of
         * rotation.
         * @since 1.2
         */
        static getRotateInstance$double$double$double(theta: number, anchorx: number, anchory: number): AffineTransform;
        /**
         * Returns a transform that rotates coordinates according to
         * a rotation vector.
         * All coordinates rotate about the origin by the same amount.
         * The amount of rotation is such that coordinates along the former
         * positive X axis will subsequently align with the vector pointing
         * from the origin to the specified vector coordinates.
         * If both <code>vecx</code> and <code>vecy</code> are 0.0,
         * an identity transform is returned.
         * This operation is equivalent to calling:
         * <pre>
         * AffineTransform.getRotateInstance(Math.atan2(vecy, vecx));
         * </pre>
         *
         * @param vecx the X coordinate of the rotation vector
         * @param vecy the Y coordinate of the rotation vector
         * @return an <code>AffineTransform</code> object that rotates
         * coordinates according to the specified rotation vector.
         * @since 1.6
         */
        static getRotateInstance$double$double(vecx: number, vecy: number): AffineTransform;
        /**
         * Returns a transform that rotates coordinates around an anchor
         * point according to a rotation vector.
         * All coordinates rotate about the specified anchor coordinates
         * by the same amount.
         * The amount of rotation is such that coordinates along the former
         * positive X axis will subsequently align with the vector pointing
         * from the origin to the specified vector coordinates.
         * If both <code>vecx</code> and <code>vecy</code> are 0.0,
         * an identity transform is returned.
         * This operation is equivalent to calling:
         * <pre>
         * AffineTransform.getRotateInstance(Math.atan2(vecy, vecx),
         * anchorx, anchory);
         * </pre>
         *
         * @param vecx the X coordinate of the rotation vector
         * @param vecy the Y coordinate of the rotation vector
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @return an <code>AffineTransform</code> object that rotates
         * coordinates around the specified point according to the
         * specified rotation vector.
         * @since 1.6
         */
        static getRotateInstance(vecx?: any, vecy?: any, anchorx?: any, anchory?: any): any;
        /**
         * Returns a transform that rotates coordinates by the specified
         * number of quadrants.
         * This operation is equivalent to calling:
         * <pre>
         * AffineTransform.getRotateInstance(numquadrants * Math.PI / 2.0);
         * </pre>
         * Rotating by a positive number of quadrants rotates points on
         * the positive X axis toward the positive Y axis.
         * @param numquadrants the number of 90 degree arcs to rotate by
         * @return an <code>AffineTransform</code> object that rotates
         * coordinates by the specified number of quadrants.
         * @since 1.6
         */
        static getQuadrantRotateInstance$int(numquadrants: number): AffineTransform;
        /**
         * Returns a transform that rotates coordinates by the specified
         * number of quadrants around the specified anchor point.
         * This operation is equivalent to calling:
         * <pre>
         * AffineTransform.getRotateInstance(numquadrants * Math.PI / 2.0,
         * anchorx, anchory);
         * </pre>
         * Rotating by a positive number of quadrants rotates points on
         * the positive X axis toward the positive Y axis.
         *
         * @param numquadrants the number of 90 degree arcs to rotate by
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @return an <code>AffineTransform</code> object that rotates
         * coordinates by the specified number of quadrants around the
         * specified anchor point.
         * @since 1.6
         */
        static getQuadrantRotateInstance(numquadrants?: any, anchorx?: any, anchory?: any): any;
        /**
         * Returns a transform representing a scaling transformation.
         * The matrix representing the returned transform is:
         * <pre>
         * [   sx   0    0   ]
         * [   0    sy   0   ]
         * [   0    0    1   ]
         * </pre>
         * @param sx the factor by which coordinates are scaled along the
         * X axis direction
         * @param sy the factor by which coordinates are scaled along the
         * Y axis direction
         * @return an <code>AffineTransform</code> object that scales
         * coordinates by the specified factors.
         * @since 1.2
         */
        static getScaleInstance(sx: number, sy: number): AffineTransform;
        /**
         * Returns a transform representing a shearing transformation.
         * The matrix representing the returned transform is:
         * <pre>
         * [   1   shx   0   ]
         * [  shy   1    0   ]
         * [   0    0    1   ]
         * </pre>
         * @param shx the multiplier by which coordinates are shifted in the
         * direction of the positive X axis as a factor of their Y coordinate
         * @param shy the multiplier by which coordinates are shifted in the
         * direction of the positive Y axis as a factor of their X coordinate
         * @return an <code>AffineTransform</code> object that shears
         * coordinates by the specified multipliers.
         * @since 1.2
         */
        static getShearInstance(shx: number, shy: number): AffineTransform;
        /**
         * Retrieves the flag bits describing the conversion properties of
         * this transform.
         * The return value is either one of the constants TYPE_IDENTITY
         * or TYPE_GENERAL_TRANSFORM, or a combination of the
         * appropriate flag bits.
         * A valid combination of flag bits is an exclusive OR operation
         * that can combine
         * the TYPE_TRANSLATION flag bit
         * in addition to either of the
         * TYPE_UNIFORM_SCALE or TYPE_GENERAL_SCALE flag bits
         * as well as either of the
         * TYPE_QUADRANT_ROTATION or TYPE_GENERAL_ROTATION flag bits.
         * @return the OR combination of any of the indicated flags that
         * apply to this transform
         * @see #TYPE_IDENTITY
         * @see #TYPE_TRANSLATION
         * @see #TYPE_UNIFORM_SCALE
         * @see #TYPE_GENERAL_SCALE
         * @see #TYPE_QUADRANT_ROTATION
         * @see #TYPE_GENERAL_ROTATION
         * @see #TYPE_GENERAL_TRANSFORM
         * @since 1.2
         */
        getType(): number;
        /**
         * This is the utility function to calculate the flag bits when
         * they have not been cached.
         * @see #getType
         */
        private calculateType();
        /**
         * Returns the determinant of the matrix representation of the transform.
         * The determinant is useful both to determine if the transform can
         * be inverted and to get a single value representing the
         * combined X and Y scaling of the transform.
         * <p>
         * If the determinant is non-zero, then this transform is
         * invertible and the various methods that depend on the inverse
         * transform do not need to throw a
         * {@link NoninvertibleTransformException}.
         * If the determinant is zero then this transform can not be
         * inverted since the transform maps all input coordinates onto
         * a line or a point.
         * If the determinant is near enough to zero then inverse transform
         * operations might not carry enough precision to produce meaningful
         * results.
         * <p>
         * If this transform represents a uniform scale, as indicated by
         * the <code>getType</code> method then the determinant also
         * represents the square of the uniform scale factor by which all of
         * the points are expanded from or contracted towards the origin.
         * If this transform represents a non-uniform scale or more general
         * transform then the determinant is not likely to represent a
         * value useful for any purpose other than determining if inverse
         * transforms are possible.
         * <p>
         * Mathematically, the determinant is calculated using the formula:
         * <pre>
         * |  m00  m01  m02  |
         * |  m10  m11  m12  |  =  m00 * m11 - m01 * m10
         * |   0    0    1   |
         * </pre>
         *
         * @return the determinant of the matrix used to transform the
         * coordinates.
         * @see #getType
         * @see #createInverse
         * @see #inverseTransform
         * @see #TYPE_UNIFORM_SCALE
         * @since 1.2
         */
        getDeterminant(): number;
        /**
         * Manually recalculates the state of the transform when the matrix
         * changes too much to predict the effects on the state.
         * The following table specifies what the various settings of the
         * state field say about the values of the corresponding matrix
         * element fields.
         * Note that the rules governing the SCALE fields are slightly
         * different depending on whether the SHEAR flag is also set.
         * <pre>
         * SCALE            SHEAR          TRANSLATE
         * m00/m11          m01/m10          m02/m12
         *
         * IDENTITY             1.0              0.0              0.0
         * TRANSLATE (TR)       1.0              0.0          not both 0.0
         * SCALE (SC)       not both 1.0         0.0              0.0
         * TR | SC          not both 1.0         0.0          not both 0.0
         * SHEAR (SH)           0.0          not both 0.0         0.0
         * TR | SH              0.0          not both 0.0     not both 0.0
         * SC | SH          not both 0.0     not both 0.0         0.0
         * TR | SC | SH     not both 0.0     not both 0.0     not both 0.0
         * </pre>
         */
        updateState(): void;
        private stateError();
        /**
         * Retrieves the 6 specifiable values in the 3x3 affine transformation
         * matrix and places them into an array of double precisions values.
         * The values are stored in the array as
         * {&nbsp;m00&nbsp;m10&nbsp;m01&nbsp;m11&nbsp;m02&nbsp;m12&nbsp;}.
         * An array of 4 doubles can also be specified, in which case only the
         * first four elements representing the non-transform
         * parts of the array are retrieved and the values are stored into
         * the array as {&nbsp;m00&nbsp;m10&nbsp;m01&nbsp;m11&nbsp;}
         * @param flatmatrix the double array used to store the returned
         * values.
         * @see #getScaleX
         * @see #getScaleY
         * @see #getShearX
         * @see #getShearY
         * @see #getTranslateX
         * @see #getTranslateY
         * @since 1.2
         */
        getMatrix(flatmatrix: number[]): void;
        /**
         * Returns the X coordinate scaling element (m00) of the 3x3
         * affine transformation matrix.
         * @return a double value that is the X coordinate of the scaling
         * element of the affine transformation matrix.
         * @see #getMatrix
         * @since 1.2
         */
        getScaleX(): number;
        /**
         * Returns the Y coordinate scaling element (m11) of the 3x3
         * affine transformation matrix.
         * @return a double value that is the Y coordinate of the scaling
         * element of the affine transformation matrix.
         * @see #getMatrix
         * @since 1.2
         */
        getScaleY(): number;
        /**
         * Returns the X coordinate shearing element (m01) of the 3x3
         * affine transformation matrix.
         * @return a double value that is the X coordinate of the shearing
         * element of the affine transformation matrix.
         * @see #getMatrix
         * @since 1.2
         */
        getShearX(): number;
        /**
         * Returns the Y coordinate shearing element (m10) of the 3x3
         * affine transformation matrix.
         * @return a double value that is the Y coordinate of the shearing
         * element of the affine transformation matrix.
         * @see #getMatrix
         * @since 1.2
         */
        getShearY(): number;
        /**
         * Returns the X coordinate of the translation element (m02) of the
         * 3x3 affine transformation matrix.
         * @return a double value that is the X coordinate of the translation
         * element of the affine transformation matrix.
         * @see #getMatrix
         * @since 1.2
         */
        getTranslateX(): number;
        /**
         * Returns the Y coordinate of the translation element (m12) of the
         * 3x3 affine transformation matrix.
         * @return a double value that is the Y coordinate of the translation
         * element of the affine transformation matrix.
         * @see #getMatrix
         * @since 1.2
         */
        getTranslateY(): number;
        /**
         * Concatenates this transform with a translation transformation.
         * This is equivalent to calling concatenate(T), where T is an
         * <code>AffineTransform</code> represented by the following matrix:
         * <pre>
         * [   1    0    tx  ]
         * [   0    1    ty  ]
         * [   0    0    1   ]
         * </pre>
         * @param tx the distance by which coordinates are translated in the
         * X axis direction
         * @param ty the distance by which coordinates are translated in the
         * Y axis direction
         * @since 1.2
         */
        translate(tx: number, ty: number): void;
        static rot90conversion: number[];
        static rot90conversion_$LI$(): number[];
        private rotate90();
        private rotate180();
        private rotate270();
        /**
         * Concatenates this transform with a rotation transformation.
         * This is equivalent to calling concatenate(R), where R is an
         * <code>AffineTransform</code> represented by the following matrix:
         * <pre>
         * [   cos(theta)    -sin(theta)    0   ]
         * [   sin(theta)     cos(theta)    0   ]
         * [       0              0         1   ]
         * </pre>
         * Rotating by a positive angle theta rotates points on the positive
         * X axis toward the positive Y axis.
         * Note also the discussion of
         * <a href="#quadrantapproximation">Handling 90-Degree Rotations</a>
         * above.
         * @param theta the angle of rotation measured in radians
         * @since 1.2
         */
        rotate$double(theta: number): void;
        /**
         * Concatenates this transform with a transform that rotates
         * coordinates around an anchor point.
         * This operation is equivalent to translating the coordinates so
         * that the anchor point is at the origin (S1), then rotating them
         * about the new origin (S2), and finally translating so that the
         * intermediate origin is restored to the coordinates of the original
         * anchor point (S3).
         * <p>
         * This operation is equivalent to the following sequence of calls:
         * <pre>
         * translate(anchorx, anchory);      // S3: final translation
         * rotate(theta);                    // S2: rotate around anchor
         * translate(-anchorx, -anchory);    // S1: translate anchor to origin
         * </pre>
         * Rotating by a positive angle theta rotates points on the positive
         * X axis toward the positive Y axis.
         * Note also the discussion of
         * <a href="#quadrantapproximation">Handling 90-Degree Rotations</a>
         * above.
         *
         * @param theta the angle of rotation measured in radians
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @since 1.2
         */
        rotate$double$double$double(theta: number, anchorx: number, anchory: number): void;
        /**
         * Concatenates this transform with a transform that rotates
         * coordinates according to a rotation vector.
         * All coordinates rotate about the origin by the same amount.
         * The amount of rotation is such that coordinates along the former
         * positive X axis will subsequently align with the vector pointing
         * from the origin to the specified vector coordinates.
         * If both <code>vecx</code> and <code>vecy</code> are 0.0,
         * no additional rotation is added to this transform.
         * This operation is equivalent to calling:
         * <pre>
         * rotate(Math.atan2(vecy, vecx));
         * </pre>
         *
         * @param vecx the X coordinate of the rotation vector
         * @param vecy the Y coordinate of the rotation vector
         * @since 1.6
         */
        rotate$double$double(vecx: number, vecy: number): void;
        /**
         * Concatenates this transform with a transform that rotates
         * coordinates around an anchor point according to a rotation
         * vector.
         * All coordinates rotate about the specified anchor coordinates
         * by the same amount.
         * The amount of rotation is such that coordinates along the former
         * positive X axis will subsequently align with the vector pointing
         * from the origin to the specified vector coordinates.
         * If both <code>vecx</code> and <code>vecy</code> are 0.0,
         * the transform is not modified in any way.
         * This method is equivalent to calling:
         * <pre>
         * rotate(Math.atan2(vecy, vecx), anchorx, anchory);
         * </pre>
         *
         * @param vecx the X coordinate of the rotation vector
         * @param vecy the Y coordinate of the rotation vector
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @since 1.6
         */
        rotate(vecx?: any, vecy?: any, anchorx?: any, anchory?: any): any;
        /**
         * Concatenates this transform with a transform that rotates
         * coordinates by the specified number of quadrants.
         * This is equivalent to calling:
         * <pre>
         * rotate(numquadrants * Math.PI / 2.0);
         * </pre>
         * Rotating by a positive number of quadrants rotates points on
         * the positive X axis toward the positive Y axis.
         * @param numquadrants the number of 90 degree arcs to rotate by
         * @since 1.6
         */
        quadrantRotate$int(numquadrants: number): void;
        /**
         * Concatenates this transform with a transform that rotates
         * coordinates by the specified number of quadrants around
         * the specified anchor point.
         * This method is equivalent to calling:
         * <pre>
         * rotate(numquadrants * Math.PI / 2.0, anchorx, anchory);
         * </pre>
         * Rotating by a positive number of quadrants rotates points on
         * the positive X axis toward the positive Y axis.
         *
         * @param numquadrants the number of 90 degree arcs to rotate by
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @since 1.6
         */
        quadrantRotate(numquadrants?: any, anchorx?: any, anchory?: any): any;
        /**
         * Concatenates this transform with a scaling transformation.
         * This is equivalent to calling concatenate(S), where S is an
         * <code>AffineTransform</code> represented by the following matrix:
         * <pre>
         * [   sx   0    0   ]
         * [   0    sy   0   ]
         * [   0    0    1   ]
         * </pre>
         * @param sx the factor by which coordinates are scaled along the
         * X axis direction
         * @param sy the factor by which coordinates are scaled along the
         * Y axis direction
         * @since 1.2
         */
        scale(sx: number, sy: number): void;
        /**
         * Concatenates this transform with a shearing transformation.
         * This is equivalent to calling concatenate(SH), where SH is an
         * <code>AffineTransform</code> represented by the following matrix:
         * <pre>
         * [   1   shx   0   ]
         * [  shy   1    0   ]
         * [   0    0    1   ]
         * </pre>
         * @param shx the multiplier by which coordinates are shifted in the
         * direction of the positive X axis as a factor of their Y coordinate
         * @param shy the multiplier by which coordinates are shifted in the
         * direction of the positive Y axis as a factor of their X coordinate
         * @since 1.2
         */
        shear(shx: number, shy: number): void;
        /**
         * Resets this transform to the Identity transform.
         * @since 1.2
         */
        setToIdentity(): void;
        /**
         * Sets this transform to a translation transformation.
         * The matrix representing this transform becomes:
         * <pre>
         * [   1    0    tx  ]
         * [   0    1    ty  ]
         * [   0    0    1   ]
         * </pre>
         * @param tx the distance by which coordinates are translated in the
         * X axis direction
         * @param ty the distance by which coordinates are translated in the
         * Y axis direction
         * @since 1.2
         */
        setToTranslation(tx: number, ty: number): void;
        /**
         * Sets this transform to a rotation transformation.
         * The matrix representing this transform becomes:
         * <pre>
         * [   cos(theta)    -sin(theta)    0   ]
         * [   sin(theta)     cos(theta)    0   ]
         * [       0              0         1   ]
         * </pre>
         * Rotating by a positive angle theta rotates points on the positive
         * X axis toward the positive Y axis.
         * Note also the discussion of
         * <a href="#quadrantapproximation">Handling 90-Degree Rotations</a>
         * above.
         * @param theta the angle of rotation measured in radians
         * @since 1.2
         */
        setToRotation$double(theta: number): void;
        /**
         * Sets this transform to a translated rotation transformation.
         * This operation is equivalent to translating the coordinates so
         * that the anchor point is at the origin (S1), then rotating them
         * about the new origin (S2), and finally translating so that the
         * intermediate origin is restored to the coordinates of the original
         * anchor point (S3).
         * <p>
         * This operation is equivalent to the following sequence of calls:
         * <pre>
         * setToTranslation(anchorx, anchory); // S3: final translation
         * rotate(theta);                      // S2: rotate around anchor
         * translate(-anchorx, -anchory);      // S1: translate anchor to origin
         * </pre>
         * The matrix representing this transform becomes:
         * <pre>
         * [   cos(theta)    -sin(theta)    x-x*cos+y*sin  ]
         * [   sin(theta)     cos(theta)    y-x*sin-y*cos  ]
         * [       0              0               1        ]
         * </pre>
         * Rotating by a positive angle theta rotates points on the positive
         * X axis toward the positive Y axis.
         * Note also the discussion of
         * <a href="#quadrantapproximation">Handling 90-Degree Rotations</a>
         * above.
         *
         * @param theta the angle of rotation measured in radians
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @since 1.2
         */
        setToRotation$double$double$double(theta: number, anchorx: number, anchory: number): void;
        /**
         * Sets this transform to a rotation transformation that rotates
         * coordinates according to a rotation vector.
         * All coordinates rotate about the origin by the same amount.
         * The amount of rotation is such that coordinates along the former
         * positive X axis will subsequently align with the vector pointing
         * from the origin to the specified vector coordinates.
         * If both <code>vecx</code> and <code>vecy</code> are 0.0,
         * the transform is set to an identity transform.
         * This operation is equivalent to calling:
         * <pre>
         * setToRotation(Math.atan2(vecy, vecx));
         * </pre>
         *
         * @param vecx the X coordinate of the rotation vector
         * @param vecy the Y coordinate of the rotation vector
         * @since 1.6
         */
        setToRotation$double$double(vecx: number, vecy: number): void;
        /**
         * Sets this transform to a rotation transformation that rotates
         * coordinates around an anchor point according to a rotation
         * vector.
         * All coordinates rotate about the specified anchor coordinates
         * by the same amount.
         * The amount of rotation is such that coordinates along the former
         * positive X axis will subsequently align with the vector pointing
         * from the origin to the specified vector coordinates.
         * If both <code>vecx</code> and <code>vecy</code> are 0.0,
         * the transform is set to an identity transform.
         * This operation is equivalent to calling:
         * <pre>
         * setToTranslation(Math.atan2(vecy, vecx), anchorx, anchory);
         * </pre>
         *
         * @param vecx the X coordinate of the rotation vector
         * @param vecy the Y coordinate of the rotation vector
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @since 1.6
         */
        setToRotation(vecx?: any, vecy?: any, anchorx?: any, anchory?: any): any;
        /**
         * Sets this transform to a rotation transformation that rotates
         * coordinates by the specified number of quadrants.
         * This operation is equivalent to calling:
         * <pre>
         * setToRotation(numquadrants * Math.PI / 2.0);
         * </pre>
         * Rotating by a positive number of quadrants rotates points on
         * the positive X axis toward the positive Y axis.
         * @param numquadrants the number of 90 degree arcs to rotate by
         * @since 1.6
         */
        setToQuadrantRotation$int(numquadrants: number): void;
        /**
         * Sets this transform to a translated rotation transformation
         * that rotates coordinates by the specified number of quadrants
         * around the specified anchor point.
         * This operation is equivalent to calling:
         * <pre>
         * setToRotation(numquadrants * Math.PI / 2.0, anchorx, anchory);
         * </pre>
         * Rotating by a positive number of quadrants rotates points on
         * the positive X axis toward the positive Y axis.
         *
         * @param numquadrants the number of 90 degree arcs to rotate by
         * @param anchorx the X coordinate of the rotation anchor point
         * @param anchory the Y coordinate of the rotation anchor point
         * @since 1.6
         */
        setToQuadrantRotation(numquadrants?: any, anchorx?: any, anchory?: any): any;
        /**
         * Sets this transform to a scaling transformation.
         * The matrix representing this transform becomes:
         * <pre>
         * [   sx   0    0   ]
         * [   0    sy   0   ]
         * [   0    0    1   ]
         * </pre>
         * @param sx the factor by which coordinates are scaled along the
         * X axis direction
         * @param sy the factor by which coordinates are scaled along the
         * Y axis direction
         * @since 1.2
         */
        setToScale(sx: number, sy: number): void;
        /**
         * Sets this transform to a shearing transformation.
         * The matrix representing this transform becomes:
         * <pre>
         * [   1   shx   0   ]
         * [  shy   1    0   ]
         * [   0    0    1   ]
         * </pre>
         * @param shx the multiplier by which coordinates are shifted in the
         * direction of the positive X axis as a factor of their Y coordinate
         * @param shy the multiplier by which coordinates are shifted in the
         * direction of the positive Y axis as a factor of their X coordinate
         * @since 1.2
         */
        setToShear(shx: number, shy: number): void;
        /**
         * Sets this transform to a copy of the transform in the specified
         * <code>AffineTransform</code> object.
         * @param Tx the <code>AffineTransform</code> object from which to
         * copy the transform
         * @since 1.2
         */
        setTransform$java_awt_geom_AffineTransform(Tx: AffineTransform): void;
        /**
         * Sets this transform to the matrix specified by the 6
         * double precision values.
         *
         * @param m00 the X coordinate scaling element of the 3x3 matrix
         * @param m10 the Y coordinate shearing element of the 3x3 matrix
         * @param m01 the X coordinate shearing element of the 3x3 matrix
         * @param m11 the Y coordinate scaling element of the 3x3 matrix
         * @param m02 the X coordinate translation element of the 3x3 matrix
         * @param m12 the Y coordinate translation element of the 3x3 matrix
         * @since 1.2
         */
        setTransform(m00?: any, m10?: any, m01?: any, m11?: any, m02?: any, m12?: any): any;
        /**
         * Concatenates an <code>AffineTransform</code> <code>Tx</code> to
         * this <code>AffineTransform</code> Cx in the most commonly useful
         * way to provide a new user space
         * that is mapped to the former user space by <code>Tx</code>.
         * Cx is updated to perform the combined transformation.
         * Transforming a point p by the updated transform Cx' is
         * equivalent to first transforming p by <code>Tx</code> and then
         * transforming the result by the original transform Cx like this:
         * Cx'(p) = Cx(Tx(p))
         * In matrix notation, if this transform Cx is
         * represented by the matrix [this] and <code>Tx</code> is represented
         * by the matrix [Tx] then this method does the following:
         * <pre>
         * [this] = [this] x [Tx]
         * </pre>
         * @param Tx the <code>AffineTransform</code> object to be
         * concatenated with this <code>AffineTransform</code> object.
         * @see #preConcatenate
         * @since 1.2
         */
        concatenate(Tx: AffineTransform): void;
        /**
         * Concatenates an <code>AffineTransform</code> <code>Tx</code> to
         * this <code>AffineTransform</code> Cx
         * in a less commonly used way such that <code>Tx</code> modifies the
         * coordinate transformation relative to the absolute pixel
         * space rather than relative to the existing user space.
         * Cx is updated to perform the combined transformation.
         * Transforming a point p by the updated transform Cx' is
         * equivalent to first transforming p by the original transform
         * Cx and then transforming the result by
         * <code>Tx</code> like this:
         * Cx'(p) = Tx(Cx(p))
         * In matrix notation, if this transform Cx
         * is represented by the matrix [this] and <code>Tx</code> is
         * represented by the matrix [Tx] then this method does the
         * following:
         * <pre>
         * [this] = [Tx] x [this]
         * </pre>
         * @param Tx the <code>AffineTransform</code> object to be
         * concatenated with this <code>AffineTransform</code> object.
         * @see #concatenate
         * @since 1.2
         */
        preConcatenate(Tx: AffineTransform): void;
        /**
         * Returns an <code>AffineTransform</code> object representing the
         * inverse transformation.
         * The inverse transform Tx' of this transform Tx
         * maps coordinates transformed by Tx back
         * to their original coordinates.
         * In other words, Tx'(Tx(p)) = p = Tx(Tx'(p)).
         * <p>
         * If this transform maps all coordinates onto a point or a line
         * then it will not have an inverse, since coordinates that do
         * not lie on the destination point or line will not have an inverse
         * mapping.
         * The <code>getDeterminant</code> method can be used to determine if this
         * transform has no inverse, in which case an exception will be
         * thrown if the <code>createInverse</code> method is called.
         * @return a new <code>AffineTransform</code> object representing the
         * inverse transformation.
         * @see #getDeterminant
         * @exception NoninvertibleTransformException
         * if the matrix cannot be inverted.
         * @since 1.2
         */
        createInverse(): AffineTransform;
        /**
         * Sets this transform to the inverse of itself.
         * The inverse transform Tx' of this transform Tx
         * maps coordinates transformed by Tx back
         * to their original coordinates.
         * In other words, Tx'(Tx(p)) = p = Tx(Tx'(p)).
         * <p>
         * If this transform maps all coordinates onto a point or a line
         * then it will not have an inverse, since coordinates that do
         * not lie on the destination point or line will not have an inverse
         * mapping.
         * The <code>getDeterminant</code> method can be used to determine if this
         * transform has no inverse, in which case an exception will be
         * thrown if the <code>invert</code> method is called.
         * @see #getDeterminant
         * @exception NoninvertibleTransformException
         * if the matrix cannot be inverted.
         * @since 1.6
         */
        invert(): void;
        /**
         * Transforms the specified <code>ptSrc</code> and stores the result
         * in <code>ptDst</code>.
         * If <code>ptDst</code> is <code>null</code>, a new {@link Point2D}
         * object is allocated and then the result of the transformation is
         * stored in this object.
         * In either case, <code>ptDst</code>, which contains the
         * transformed point, is returned for convenience.
         * If <code>ptSrc</code> and <code>ptDst</code> are the same
         * object, the input point is correctly overwritten with
         * the transformed point.
         * @param ptSrc the specified <code>Point2D</code> to be transformed
         * @param ptDst the specified <code>Point2D</code> that stores the
         * result of transforming <code>ptSrc</code>
         * @return the <code>ptDst</code> after transforming
         * <code>ptSrc</code> and storing the result in <code>ptDst</code>.
         * @since 1.2
         */
        transform$java_awt_geom_Point2D$java_awt_geom_Point2D(ptSrc: java.awt.geom.Point2D, ptDst: java.awt.geom.Point2D): java.awt.geom.Point2D;
        /**
         * Transforms an array of point objects by this transform.
         * If any element of the <code>ptDst</code> array is
         * <code>null</code>, a new <code>Point2D</code> object is allocated
         * and stored into that element before storing the results of the
         * transformation.
         * <p>
         * Note that this method does not take any precautions to
         * avoid problems caused by storing results into <code>Point2D</code>
         * objects that will be used as the source for calculations
         * further down the source array.
         * This method does guarantee that if a specified <code>Point2D</code>
         * object is both the source and destination for the same single point
         * transform operation then the results will not be stored until
         * the calculations are complete to avoid storing the results on
         * top of the operands.
         * If, however, the destination <code>Point2D</code> object for one
         * operation is the same object as the source <code>Point2D</code>
         * object for another operation further down the source array then
         * the original coordinates in that point are overwritten before
         * they can be converted.
         * @param ptSrc the array containing the source point objects
         * @param ptDst the array into which the transform point objects are
         * returned
         * @param srcOff the offset to the first point object to be
         * transformed in the source array
         * @param dstOff the offset to the location of the first
         * transformed point object that is stored in the destination array
         * @param numPts the number of point objects to be transformed
         * @since 1.2
         */
        transform(ptSrc?: any, srcOff?: any, ptDst?: any, dstOff?: any, numPts?: any): any;
        /**
         * Transforms an array of floating point coordinates by this transform.
         * The two coordinate array sections can be exactly the same or
         * can be overlapping sections of the same array without affecting the
         * validity of the results.
         * This method ensures that no source coordinates are overwritten by a
         * previous operation before they can be transformed.
         * The coordinates are stored in the arrays starting at the specified
         * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
         * @param srcPts the array containing the source point coordinates.
         * Each point is stored as a pair of x,&nbsp;y coordinates.
         * @param dstPts the array into which the transformed point coordinates
         * are returned.  Each point is stored as a pair of x,&nbsp;y
         * coordinates.
         * @param srcOff the offset to the first point to be transformed
         * in the source array
         * @param dstOff the offset to the location of the first
         * transformed point that is stored in the destination array
         * @param numPts the number of points to be transformed
         * @since 1.2
         */
        transform$float_A$int$float_A$int$int(srcPts: number[], srcOff: number, dstPts: number[], dstOff: number, numPts: number): void;
        /**
         * Transforms an array of double precision coordinates by this transform.
         * The two coordinate array sections can be exactly the same or
         * can be overlapping sections of the same array without affecting the
         * validity of the results.
         * This method ensures that no source coordinates are
         * overwritten by a previous operation before they can be transformed.
         * The coordinates are stored in the arrays starting at the indicated
         * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
         * @param srcPts the array containing the source point coordinates.
         * Each point is stored as a pair of x,&nbsp;y coordinates.
         * @param dstPts the array into which the transformed point
         * coordinates are returned.  Each point is stored as a pair of
         * x,&nbsp;y coordinates.
         * @param srcOff the offset to the first point to be transformed
         * in the source array
         * @param dstOff the offset to the location of the first
         * transformed point that is stored in the destination array
         * @param numPts the number of point objects to be transformed
         * @since 1.2
         */
        transform$double_A$int$double_A$int$int(srcPts: number[], srcOff: number, dstPts: number[], dstOff: number, numPts: number): void;
        /**
         * Transforms an array of floating point coordinates by this transform
         * and stores the results into an array of doubles.
         * The coordinates are stored in the arrays starting at the specified
         * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
         * @param srcPts the array containing the source point coordinates.
         * Each point is stored as a pair of x,&nbsp;y coordinates.
         * @param dstPts the array into which the transformed point coordinates
         * are returned.  Each point is stored as a pair of x,&nbsp;y
         * coordinates.
         * @param srcOff the offset to the first point to be transformed
         * in the source array
         * @param dstOff the offset to the location of the first
         * transformed point that is stored in the destination array
         * @param numPts the number of points to be transformed
         * @since 1.2
         */
        transform$float_A$int$double_A$int$int(srcPts: number[], srcOff: number, dstPts: number[], dstOff: number, numPts: number): void;
        /**
         * Transforms an array of double precision coordinates by this transform
         * and stores the results into an array of floats.
         * The coordinates are stored in the arrays starting at the specified
         * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
         * @param srcPts the array containing the source point coordinates.
         * Each point is stored as a pair of x,&nbsp;y coordinates.
         * @param dstPts the array into which the transformed point
         * coordinates are returned.  Each point is stored as a pair of
         * x,&nbsp;y coordinates.
         * @param srcOff the offset to the first point to be transformed
         * in the source array
         * @param dstOff the offset to the location of the first
         * transformed point that is stored in the destination array
         * @param numPts the number of point objects to be transformed
         * @since 1.2
         */
        transform$double_A$int$float_A$int$int(srcPts: number[], srcOff: number, dstPts: number[], dstOff: number, numPts: number): void;
        /**
         * Inverse transforms the specified <code>ptSrc</code> and stores the
         * result in <code>ptDst</code>.
         * If <code>ptDst</code> is <code>null</code>, a new
         * <code>Point2D</code> object is allocated and then the result of the
         * transform is stored in this object.
         * In either case, <code>ptDst</code>, which contains the transformed
         * point, is returned for convenience.
         * If <code>ptSrc</code> and <code>ptDst</code> are the same
         * object, the input point is correctly overwritten with the
         * transformed point.
         * @param ptSrc the point to be inverse transformed
         * @param ptDst the resulting transformed point
         * @return <code>ptDst</code>, which contains the result of the
         * inverse transform.
         * @exception NoninvertibleTransformException  if the matrix cannot be
         * inverted.
         * @since 1.2
         */
        inverseTransform$java_awt_geom_Point2D$java_awt_geom_Point2D(ptSrc: java.awt.geom.Point2D, ptDst: java.awt.geom.Point2D): java.awt.geom.Point2D;
        /**
         * Inverse transforms an array of double precision coordinates by
         * this transform.
         * The two coordinate array sections can be exactly the same or
         * can be overlapping sections of the same array without affecting the
         * validity of the results.
         * This method ensures that no source coordinates are
         * overwritten by a previous operation before they can be transformed.
         * The coordinates are stored in the arrays starting at the specified
         * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
         * @param srcPts the array containing the source point coordinates.
         * Each point is stored as a pair of x,&nbsp;y coordinates.
         * @param dstPts the array into which the transformed point
         * coordinates are returned.  Each point is stored as a pair of
         * x,&nbsp;y coordinates.
         * @param srcOff the offset to the first point to be transformed
         * in the source array
         * @param dstOff the offset to the location of the first
         * transformed point that is stored in the destination array
         * @param numPts the number of point objects to be transformed
         * @exception NoninvertibleTransformException  if the matrix cannot be
         * inverted.
         * @since 1.2
         */
        inverseTransform(srcPts?: any, srcOff?: any, dstPts?: any, dstOff?: any, numPts?: any): any;
        /**
         * Transforms the relative distance vector specified by
         * <code>ptSrc</code> and stores the result in <code>ptDst</code>.
         * A relative distance vector is transformed without applying the
         * translation components of the affine transformation matrix
         * using the following equations:
         * <pre>
         * [  x' ]   [  m00  m01 (m02) ] [  x  ]   [ m00x + m01y ]
         * [  y' ] = [  m10  m11 (m12) ] [  y  ] = [ m10x + m11y ]
         * [ (1) ]   [  (0)  (0) ( 1 ) ] [ (1) ]   [     (1)     ]
         * </pre>
         * If <code>ptDst</code> is <code>null</code>, a new
         * <code>Point2D</code> object is allocated and then the result of the
         * transform is stored in this object.
         * In either case, <code>ptDst</code>, which contains the
         * transformed point, is returned for convenience.
         * If <code>ptSrc</code> and <code>ptDst</code> are the same object,
         * the input point is correctly overwritten with the transformed
         * point.
         * @param ptSrc the distance vector to be delta transformed
         * @param ptDst the resulting transformed distance vector
         * @return <code>ptDst</code>, which contains the result of the
         * transformation.
         * @since 1.2
         */
        deltaTransform$java_awt_geom_Point2D$java_awt_geom_Point2D(ptSrc: java.awt.geom.Point2D, ptDst: java.awt.geom.Point2D): java.awt.geom.Point2D;
        /**
         * Transforms an array of relative distance vectors by this
         * transform.
         * A relative distance vector is transformed without applying the
         * translation components of the affine transformation matrix
         * using the following equations:
         * <pre>
         * [  x' ]   [  m00  m01 (m02) ] [  x  ]   [ m00x + m01y ]
         * [  y' ] = [  m10  m11 (m12) ] [  y  ] = [ m10x + m11y ]
         * [ (1) ]   [  (0)  (0) ( 1 ) ] [ (1) ]   [     (1)     ]
         * </pre>
         * The two coordinate array sections can be exactly the same or
         * can be overlapping sections of the same array without affecting the
         * validity of the results.
         * This method ensures that no source coordinates are
         * overwritten by a previous operation before they can be transformed.
         * The coordinates are stored in the arrays starting at the indicated
         * offset in the order <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
         * @param srcPts the array containing the source distance vectors.
         * Each vector is stored as a pair of relative x,&nbsp;y coordinates.
         * @param dstPts the array into which the transformed distance vectors
         * are returned.  Each vector is stored as a pair of relative
         * x,&nbsp;y coordinates.
         * @param srcOff the offset to the first vector to be transformed
         * in the source array
         * @param dstOff the offset to the location of the first
         * transformed vector that is stored in the destination array
         * @param numPts the number of vector coordinate pairs to be
         * transformed
         * @since 1.2
         */
        deltaTransform(srcPts?: any, srcOff?: any, dstPts?: any, dstOff?: any, numPts?: any): any;
        /**
         * Returns a new {@link Shape} object defined by the geometry of the
         * specified <code>Shape</code> after it has been transformed by
         * this transform.
         * @param pSrc the specified <code>Shape</code> object to be
         * transformed by this transform.
         * @return a new <code>Shape</code> object that defines the geometry
         * of the transformed <code>Shape</code>, or null if {@code pSrc} is null.
         * @since 1.2
         */
        createTransformedShape(pSrc: java.awt.Shape): java.awt.Shape;
        private static _matround(matval);
        /**
         * Returns a <code>String</code> that represents the value of this
         * {@link Object}.
         * @return a <code>String</code> representing the value of this
         * <code>Object</code>.
         * @since 1.2
         */
        toString(): string;
        /**
         * Returns <code>true</code> if this <code>AffineTransform</code> is
         * an identity transform.
         * @return <code>true</code> if this <code>AffineTransform</code> is
         * an identity transform; <code>false</code> otherwise.
         * @since 1.2
         */
        isIdentity(): boolean;
        /**
         * Returns a copy of this <code>AffineTransform</code> object.
         * @return an <code>Object</code> that is a copy of this
         * <code>AffineTransform</code> object.
         * @since 1.2
         */
        clone(): any;
        /**
         * Returns the hashcode for this transform.
         * @return      a hash code for this transform.
         * @since 1.2
         */
        hashCode(): number;
        /**
         * Returns <code>true</code> if this <code>AffineTransform</code>
         * represents the same affine coordinate transform as the specified
         * argument.
         * @param obj the <code>Object</code> to test for equality with this
         * <code>AffineTransform</code>
         * @return <code>true</code> if <code>obj</code> equals this
         * <code>AffineTransform</code> object; <code>false</code> otherwise.
         * @since 1.2
         */
        equals(obj: any): boolean;
        static serialVersionUID: number;
    }
}
declare namespace java.awt.geom {
    /**
     * A utility class to iterate over the path segments of an arc through the
     * PathIterator interface.
     *
     * @author Jim Graham
     */
    class ArcIterator implements java.awt.geom.PathIterator {
        x: number;
        y: number;
        w: number;
        h: number;
        angStRad: number;
        increment: number;
        cv: number;
        affine: java.awt.geom.AffineTransform;
        index: number;
        arcSegs: number;
        lineSegs: number;
        constructor(a: java.awt.geom.Arc2D, at: java.awt.geom.AffineTransform);
        /**
         * Return the winding rule for determining the insideness of the path.
         *
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if there are more points to read.
         *
         * @return true if there are more points to read
         */
        isDone(): boolean;
        /**
         * Moves the iterator to the next segment of the path forwards along the
         * primary direction of traversal as long as there are more points in that
         * direction.
         */
        next(): void;
        private static btan(increment);
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of float x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment(coords?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of double x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>CubicCurve2D</code> class defines a cubic parametric curve segment
     * in {@code (x,y)} coordinate space.
     * <p>
     * This class is only the abstract superclass for all objects which store a 2D
     * cubic curve segment. The actual storage representation of the coordinates is
     * left to the subclass.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class CubicCurve2D implements java.awt.Shape, java.lang.Cloneable {
        abstract getBounds2D(): any;
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for instantiation
         * and provide a number of formats for storing the information necessary to
         * satisfy the various accessor methods below.
         *
         * @see java.awt.geom.CubicCurve2D.Float
         * @see java.awt.geom.CubicCurve2D.Double
         * @since 1.2
         */
        constructor();
        /**
         * Returns the X coordinate of the start point in double precision.
         *
         * @return the X coordinate of the start point of the {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getX1(): number;
        /**
         * Returns the Y coordinate of the start point in double precision.
         *
         * @return the Y coordinate of the start point of the {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getY1(): number;
        /**
         * Returns the start point.
         *
         * @return a {@code Point2D} that is the start point of the
         * {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getP1(): java.awt.geom.Point2D;
        /**
         * Returns the X coordinate of the first control point in double precision.
         *
         * @return the X coordinate of the first control point of the
         * {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getCtrlX1(): number;
        /**
         * Returns the Y coordinate of the first control point in double precision.
         *
         * @return the Y coordinate of the first control point of the
         * {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getCtrlY1(): number;
        /**
         * Returns the first control point.
         *
         * @return a {@code Point2D} that is the first control point of the
         * {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getCtrlP1(): java.awt.geom.Point2D;
        /**
         * Returns the X coordinate of the second control point in double precision.
         *
         * @return the X coordinate of the second control point of the
         * {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getCtrlX2(): number;
        /**
         * Returns the Y coordinate of the second control point in double precision.
         *
         * @return the Y coordinate of the second control point of the
         * {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getCtrlY2(): number;
        /**
         * Returns the second control point.
         *
         * @return a {@code Point2D} that is the second control point of the
         * {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getCtrlP2(): java.awt.geom.Point2D;
        /**
         * Returns the X coordinate of the end point in double precision.
         *
         * @return the X coordinate of the end point of the {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getX2(): number;
        /**
         * Returns the Y coordinate of the end point in double precision.
         *
         * @return the Y coordinate of the end point of the {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getY2(): number;
        /**
         * Returns the end point.
         *
         * @return a {@code Point2D} that is the end point of the
         * {@code CubicCurve2D}.
         * @since 1.2
         */
        abstract getP2(): java.awt.geom.Point2D;
        /**
         * Sets the location of the end points and control points of this curve
         * to the specified {@code float} coordinates.
         *
         * @param x1
         * the X coordinate used to set the start point of this
         * {@code CubicCurve2D}
         * @param y1
         * the Y coordinate used to set the start point of this
         * {@code CubicCurve2D}
         * @param ctrlx1
         * the X coordinate used to set the first control point of
         * this {@code CubicCurve2D}
         * @param ctrly1
         * the Y coordinate used to set the first control point of
         * this {@code CubicCurve2D}
         * @param ctrlx2
         * the X coordinate used to set the second control point of
         * this {@code CubicCurve2D}
         * @param ctrly2
         * the Y coordinate used to set the second control point of
         * this {@code CubicCurve2D}
         * @param x2
         * the X coordinate used to set the end point of this
         * {@code CubicCurve2D}
         * @param y2
         * the Y coordinate used to set the end point of this
         * {@code CubicCurve2D}
         * @since 1.2
         */
        setCurve(x1?: any, y1?: any, ctrlx1?: any, ctrly1?: any, ctrlx2?: any, ctrly2?: any, x2?: any, y2?: any): any;
        /**
         * Sets the location of the end points and control points of this curve to
         * the specified double coordinates.
         *
         * @param x1
         * the X coordinate used to set the start point of this
         * {@code CubicCurve2D}
         * @param y1
         * the Y coordinate used to set the start point of this
         * {@code CubicCurve2D}
         * @param ctrlx1
         * the X coordinate used to set the first control point of this
         * {@code CubicCurve2D}
         * @param ctrly1
         * the Y coordinate used to set the first control point of this
         * {@code CubicCurve2D}
         * @param ctrlx2
         * the X coordinate used to set the second control point of this
         * {@code CubicCurve2D}
         * @param ctrly2
         * the Y coordinate used to set the second control point of this
         * {@code CubicCurve2D}
         * @param x2
         * the X coordinate used to set the end point of this
         * {@code CubicCurve2D}
         * @param y2
         * the Y coordinate used to set the end point of this
         * {@code CubicCurve2D}
         * @since 1.2
         */
        setCurve$double$double$double$double$double$double$double$double(x1: number, y1: number, ctrlx1: number, ctrly1: number, ctrlx2: number, ctrly2: number, x2: number, y2: number): void;
        /**
         * Sets the location of the end points and control points of this curve to
         * the double coordinates at the specified offset in the specified array.
         *
         * @param coords
         * a double array containing coordinates
         * @param offset
         * the index of <code>coords</code> from which to begin setting
         * the end points and control points of this curve to the
         * coordinates contained in <code>coords</code>
         * @since 1.2
         */
        setCurve$double_A$int(coords: number[], offset: number): void;
        /**
         * Sets the location of the end points and control points of this curve to
         * the specified <code>Point2D</code> coordinates.
         *
         * @param p1
         * the first specified <code>Point2D</code> used to set the start
         * point of this curve
         * @param cp1
         * the second specified <code>Point2D</code> used to set the
         * first control point of this curve
         * @param cp2
         * the third specified <code>Point2D</code> used to set the
         * second control point of this curve
         * @param p2
         * the fourth specified <code>Point2D</code> used to set the end
         * point of this curve
         * @since 1.2
         */
        setCurve$java_awt_geom_Point2D$java_awt_geom_Point2D$java_awt_geom_Point2D$java_awt_geom_Point2D(p1: java.awt.geom.Point2D, cp1: java.awt.geom.Point2D, cp2: java.awt.geom.Point2D, p2: java.awt.geom.Point2D): void;
        /**
         * Sets the location of the end points and control points of this curve to
         * the coordinates of the <code>Point2D</code> objects at the specified
         * offset in the specified array.
         *
         * @param pts
         * an array of <code>Point2D</code> objects
         * @param offset
         * the index of <code>pts</code> from which to begin setting the
         * end points and control points of this curve to the points
         * contained in <code>pts</code>
         * @since 1.2
         */
        setCurve$java_awt_geom_Point2D_A$int(pts: java.awt.geom.Point2D[], offset: number): void;
        /**
         * Sets the location of the end points and control points of this curve to
         * the same as those in the specified <code>CubicCurve2D</code>.
         *
         * @param c
         * the specified <code>CubicCurve2D</code>
         * @since 1.2
         */
        setCurve$java_awt_geom_CubicCurve2D(c: CubicCurve2D): void;
        /**
         * Returns the square of the flatness of the cubic curve specified by the
         * indicated control points. The flatness is the maximum distance of a
         * control point from the line connecting the end points.
         *
         * @param x1
         * the X coordinate that specifies the start point of a
         * {@code CubicCurve2D}
         * @param y1
         * the Y coordinate that specifies the start point of a
         * {@code CubicCurve2D}
         * @param ctrlx1
         * the X coordinate that specifies the first control point of a
         * {@code CubicCurve2D}
         * @param ctrly1
         * the Y coordinate that specifies the first control point of a
         * {@code CubicCurve2D}
         * @param ctrlx2
         * the X coordinate that specifies the second control point of a
         * {@code CubicCurve2D}
         * @param ctrly2
         * the Y coordinate that specifies the second control point of a
         * {@code CubicCurve2D}
         * @param x2
         * the X coordinate that specifies the end point of a
         * {@code CubicCurve2D}
         * @param y2
         * the Y coordinate that specifies the end point of a
         * {@code CubicCurve2D}
         * @return the square of the flatness of the {@code CubicCurve2D}
         * represented by the specified coordinates.
         * @since 1.2
         */
        static getFlatnessSq(x1?: any, y1?: any, ctrlx1?: any, ctrly1?: any, ctrlx2?: any, ctrly2?: any, x2?: any, y2?: any): any;
        /**
         * Returns the flatness of the cubic curve specified by the indicated
         * control points. The flatness is the maximum distance of a control point
         * from the line connecting the end points.
         *
         * @param x1
         * the X coordinate that specifies the start point of a
         * {@code CubicCurve2D}
         * @param y1
         * the Y coordinate that specifies the start point of a
         * {@code CubicCurve2D}
         * @param ctrlx1
         * the X coordinate that specifies the first control point of a
         * {@code CubicCurve2D}
         * @param ctrly1
         * the Y coordinate that specifies the first control point of a
         * {@code CubicCurve2D}
         * @param ctrlx2
         * the X coordinate that specifies the second control point of a
         * {@code CubicCurve2D}
         * @param ctrly2
         * the Y coordinate that specifies the second control point of a
         * {@code CubicCurve2D}
         * @param x2
         * the X coordinate that specifies the end point of a
         * {@code CubicCurve2D}
         * @param y2
         * the Y coordinate that specifies the end point of a
         * {@code CubicCurve2D}
         * @return the flatness of the {@code CubicCurve2D} represented by the
         * specified coordinates.
         * @since 1.2
         */
        static getFlatness(x1?: any, y1?: any, ctrlx1?: any, ctrly1?: any, ctrlx2?: any, ctrly2?: any, x2?: any, y2?: any): any;
        /**
         * Returns the square of the flatness of the cubic curve specified by the
         * control points stored in the indicated array at the indicated index. The
         * flatness is the maximum distance of a control point from the line
         * connecting the end points.
         *
         * @param coords
         * an array containing coordinates
         * @param offset
         * the index of <code>coords</code> from which to begin getting
         * the end points and control points of the curve
         * @return the square of the flatness of the <code>CubicCurve2D</code>
         * specified by the coordinates in <code>coords</code> at the
         * specified offset.
         * @since 1.2
         */
        static getFlatnessSq$double_A$int(coords: number[], offset: number): number;
        /**
         * Returns the flatness of the cubic curve specified by the control points
         * stored in the indicated array at the indicated index. The flatness is the
         * maximum distance of a control point from the line connecting the end
         * points.
         *
         * @param coords
         * an array containing coordinates
         * @param offset
         * the index of <code>coords</code> from which to begin getting
         * the end points and control points of the curve
         * @return the flatness of the <code>CubicCurve2D</code> specified by the
         * coordinates in <code>coords</code> at the specified offset.
         * @since 1.2
         */
        static getFlatness$double_A$int(coords: number[], offset: number): number;
        /**
         * Returns the square of the flatness of this curve. The flatness is the
         * maximum distance of a control point from the line connecting the end
         * points.
         *
         * @return the square of the flatness of this curve.
         * @since 1.2
         */
        getFlatnessSq(): number;
        /**
         * Returns the flatness of this curve. The flatness is the maximum distance
         * of a control point from the line connecting the end points.
         *
         * @return the flatness of this curve.
         * @since 1.2
         */
        getFlatness(): number;
        /**
         * Subdivides this cubic curve and stores the resulting two subdivided
         * curves into the left and right curve parameters. Either or both of the
         * left and right objects may be the same as this object or null.
         *
         * @param left
         * the cubic curve object for storing for the left or first half
         * of the subdivided curve
         * @param right
         * the cubic curve object for storing for the right or second
         * half of the subdivided curve
         * @since 1.2
         */
        subdivide(left: CubicCurve2D, right: CubicCurve2D): void;
        /**
         * Subdivides the cubic curve specified by the <code>src</code> parameter
         * and stores the resulting two subdivided curves into the <code>left</code>
         * and <code>right</code> curve parameters. Either or both of the
         * <code>left</code> and <code>right</code> objects may be the same as the
         * <code>src</code> object or <code>null</code>.
         *
         * @param src
         * the cubic curve to be subdivided
         * @param left
         * the cubic curve object for storing the left or first half of
         * the subdivided curve
         * @param right
         * the cubic curve object for storing the right or second half of
         * the subdivided curve
         * @since 1.2
         */
        static subdivide$java_awt_geom_CubicCurve2D$java_awt_geom_CubicCurve2D$java_awt_geom_CubicCurve2D(src: CubicCurve2D, left: CubicCurve2D, right: CubicCurve2D): void;
        /**
         * Subdivides the cubic curve specified by the coordinates stored in the
         * <code>src</code> array at indices <code>srcoff</code> through (
         * <code>srcoff</code>&nbsp;+&nbsp;7) and stores the resulting two
         * subdivided curves into the two result arrays at the corresponding
         * indices. Either or both of the <code>left</code> and <code>right</code>
         * arrays may be <code>null</code> or a reference to the same array as the
         * <code>src</code> array. Note that the last point in the first subdivided
         * curve is the same as the first point in the second subdivided curve.
         * Thus, it is possible to pass the same array for <code>left</code> and
         * <code>right</code> and to use offsets, such as <code>rightoff</code>
         * equals (<code>leftoff</code> + 6), in order to avoid allocating extra
         * storage for this common point.
         *
         * @param src
         * the array holding the coordinates for the source curve
         * @param srcoff
         * the offset into the array of the beginning of the the 6 source
         * coordinates
         * @param left
         * the array for storing the coordinates for the first half of
         * the subdivided curve
         * @param leftoff
         * the offset into the array of the beginning of the the 6 left
         * coordinates
         * @param right
         * the array for storing the coordinates for the second half of
         * the subdivided curve
         * @param rightoff
         * the offset into the array of the beginning of the the 6 right
         * coordinates
         * @since 1.2
         */
        static subdivide(src?: any, srcoff?: any, left?: any, leftoff?: any, right?: any, rightoff?: any): any;
        /**
         * Solves the cubic whose coefficients are in the <code>eqn</code> array and
         * places the non-complex roots back into the same array, returning the
         * number of roots. The solved cubic is represented by the equation:
         *
         * <pre>
         * eqn = {c, b, a, d}
         * dx^3 + ax^2 + bx + c = 0
         * </pre>
         *
         * A return value of -1 is used to distinguish a constant equation that
         * might be always 0 or never 0 from an equation that has no zeroes.
         *
         * @param eqn
         * an array containing coefficients for a cubic
         * @return the number of roots, or -1 if the equation is a constant.
         * @since 1.2
         */
        static solveCubic$double_A(eqn: number[]): number;
        /**
         * Solve the cubic whose coefficients are in the <code>eqn</code> array and
         * place the non-complex roots into the <code>res</code> array, returning
         * the number of roots. The cubic solved is represented by the equation: eqn
         * = {c, b, a, d} dx^3 + ax^2 + bx + c = 0 A return value of -1 is used to
         * distinguish a constant equation, which may be always 0 or never 0, from
         * an equation which has no zeroes.
         *
         * @param eqn
         * the specified array of coefficients to use to solve the cubic
         * equation
         * @param res
         * the array that contains the non-complex roots resulting from
         * the solution of the cubic equation
         * @return the number of roots, or -1 if the equation is a constant
         * @since 1.3
         */
        static solveCubic(eqn?: any, res?: any): any;
        static fixRoots(eqn: number[], res: number[], num: number): number;
        static refineRootWithHint(eqn: number[], min: number, max: number, t: number): number;
        static bisectRootWithHint(eqn: number[], x0: number, xe: number, hint: number): number;
        static bisectRoot(eqn: number[], x0: number, xe: number): number;
        static inInterval(t: number, min: number, max: number): boolean;
        static within(x: number, y: number, err: number): boolean;
        static iszero(x: number, err: number): boolean;
        static oppositeSigns(x1: number, x2: number): boolean;
        static solveEqn(eqn: number[], order: number, t: number): number;
        static getRootUpperBound(eqn: number[]): number;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double(x: number, y: number): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$java_awt_geom_Point2D(p: java.awt.geom.Point2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains(x?: any, y?: any, w?: any, h?: any): any;
        rectCrossings(x: number, y: number, w: number, h: number): number;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        getBounds(): java.awt.Rectangle;
        /**
         * Returns an iteration object that defines the boundary of the shape. The
         * iterator for this class is not multi-threaded safe, which means that this
         * <code>CubicCurve2D</code> class does not guarantee that modifications to
         * the geometry of this <code>CubicCurve2D</code> object do not affect any
         * iterations of that geometry that are already in process.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @return the <code>PathIterator</code> object that returns the geometry of
         * the outline of this <code>CubicCurve2D</code>, one segment at a
         * time.
         * @since 1.2
         */
        getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
        /**
         * Return an iteration object that defines the boundary of the flattened
         * shape. The iterator for this class is not multi-threaded safe, which
         * means that this <code>CubicCurve2D</code> class does not guarantee that
         * modifications to the geometry of this <code>CubicCurve2D</code> object do
         * not affect any iterations of that geometry that are already in process.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @param flatness
         * the maximum amount that the control points for a given curve
         * can vary from colinear before a subdivided curve is replaced
         * by a straight line connecting the end points
         * @return the <code>PathIterator</code> object that returns the geometry of
         * the outline of this <code>CubicCurve2D</code>, one segment at a
         * time.
         * @since 1.2
         */
        getPathIterator(at?: any, flatness?: any): any;
        /**
         * Creates a new object of the same class as this object.
         *
         * @return a clone of this instance.
         * @exception OutOfMemoryError
         * if there is not enough memory.
         * @see java.lang.Cloneable
         * @since 1.2
         */
        clone(): any;
    }
    namespace CubicCurve2D {
        /**
         * A cubic parametric curve segment specified with {@code float}
         * coordinates.
         *
         * @since 1.2
         */
        class Float extends java.awt.geom.CubicCurve2D implements java.io.Serializable {
            /**
             * The X coordinate of the start point of the cubic curve segment.
             *
             * @since 1.2
             * @serial
             */
            x1: number;
            /**
             * The Y coordinate of the start point of the cubic curve segment.
             *
             * @since 1.2
             * @serial
             */
            y1: number;
            /**
             * The X coordinate of the first control point of the cubic curve
             * segment.
             *
             * @since 1.2
             * @serial
             */
            ctrlx1: number;
            /**
             * The Y coordinate of the first control point of the cubic curve
             * segment.
             *
             * @since 1.2
             * @serial
             */
            ctrly1: number;
            /**
             * The X coordinate of the second control point of the cubic curve
             * segment.
             *
             * @since 1.2
             * @serial
             */
            ctrlx2: number;
            /**
             * The Y coordinate of the second control point of the cubic curve
             * segment.
             *
             * @since 1.2
             * @serial
             */
            ctrly2: number;
            /**
             * The X coordinate of the end point of the cubic curve segment.
             *
             * @since 1.2
             * @serial
             */
            x2: number;
            /**
             * The Y coordinate of the end point of the cubic curve segment.
             *
             * @since 1.2
             * @serial
             */
            y2: number;
            /**
             * Constructs and initializes a {@code CubicCurve2D} from the specified
             * {@code float} coordinates.
             *
             * @param x1
             * the X coordinate for the start point of the resulting
             * {@code CubicCurve2D}
             * @param y1
             * the Y coordinate for the start point of the resulting
             * {@code CubicCurve2D}
             * @param ctrlx1
             * the X coordinate for the first control point of the
             * resulting {@code CubicCurve2D}
             * @param ctrly1
             * the Y coordinate for the first control point of the
             * resulting {@code CubicCurve2D}
             * @param ctrlx2
             * the X coordinate for the second control point of the
             * resulting {@code CubicCurve2D}
             * @param ctrly2
             * the Y coordinate for the second control point of the
             * resulting {@code CubicCurve2D}
             * @param x2
             * the X coordinate for the end point of the resulting
             * {@code CubicCurve2D}
             * @param y2
             * the Y coordinate for the end point of the resulting
             * {@code CubicCurve2D}
             * @since 1.2
             */
            constructor(x1?: any, y1?: any, ctrlx1?: any, ctrly1?: any, ctrlx2?: any, ctrly2?: any, x2?: any, y2?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP1(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlX1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlY1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlP1(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlX2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlY2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlP2(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP2(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setCurve$double$double$double$double$double$double$double$double(x1: number, y1: number, ctrlx1: number, ctrly1: number, ctrlx2: number, ctrly2: number, x2: number, y2: number): void;
            /**
             * Sets the location of the end points and control points of this curve
             * to the specified {@code float} coordinates.
             *
             * @param x1
             * the X coordinate used to set the start point of this
             * {@code CubicCurve2D}
             * @param y1
             * the Y coordinate used to set the start point of this
             * {@code CubicCurve2D}
             * @param ctrlx1
             * the X coordinate used to set the first control point of
             * this {@code CubicCurve2D}
             * @param ctrly1
             * the Y coordinate used to set the first control point of
             * this {@code CubicCurve2D}
             * @param ctrlx2
             * the X coordinate used to set the second control point of
             * this {@code CubicCurve2D}
             * @param ctrly2
             * the Y coordinate used to set the second control point of
             * this {@code CubicCurve2D}
             * @param x2
             * the X coordinate used to set the end point of this
             * {@code CubicCurve2D}
             * @param y2
             * the Y coordinate used to set the end point of this
             * {@code CubicCurve2D}
             * @since 1.2
             */
            setCurve(x1?: any, y1?: any, ctrlx1?: any, ctrly1?: any, ctrlx2?: any, ctrly2?: any, x2?: any, y2?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
        /**
         * A cubic parametric curve segment specified with {@code double}
         * coordinates.
         *
         * @since 1.2
         */
        class Double extends java.awt.geom.CubicCurve2D implements java.io.Serializable {
            /**
             * The X coordinate of the start point of the cubic curve segment.
             *
             * @since 1.2
             * @serial
             */
            x1: number;
            /**
             * The Y coordinate of the start point of the cubic curve segment.
             *
             * @since 1.2
             * @serial
             */
            y1: number;
            /**
             * The X coordinate of the first control point of the cubic curve
             * segment.
             *
             * @since 1.2
             * @serial
             */
            ctrlx1: number;
            /**
             * The Y coordinate of the first control point of the cubic curve
             * segment.
             *
             * @since 1.2
             * @serial
             */
            ctrly1: number;
            /**
             * The X coordinate of the second control point of the cubic curve
             * segment.
             *
             * @since 1.2
             * @serial
             */
            ctrlx2: number;
            /**
             * The Y coordinate of the second control point of the cubic curve
             * segment.
             *
             * @since 1.2
             * @serial
             */
            ctrly2: number;
            /**
             * The X coordinate of the end point of the cubic curve segment.
             *
             * @since 1.2
             * @serial
             */
            x2: number;
            /**
             * The Y coordinate of the end point of the cubic curve segment.
             *
             * @since 1.2
             * @serial
             */
            y2: number;
            /**
             * Constructs and initializes a {@code CubicCurve2D} from the specified
             * {@code double} coordinates.
             *
             * @param x1
             * the X coordinate for the start point of the resulting
             * {@code CubicCurve2D}
             * @param y1
             * the Y coordinate for the start point of the resulting
             * {@code CubicCurve2D}
             * @param ctrlx1
             * the X coordinate for the first control point of the
             * resulting {@code CubicCurve2D}
             * @param ctrly1
             * the Y coordinate for the first control point of the
             * resulting {@code CubicCurve2D}
             * @param ctrlx2
             * the X coordinate for the second control point of the
             * resulting {@code CubicCurve2D}
             * @param ctrly2
             * the Y coordinate for the second control point of the
             * resulting {@code CubicCurve2D}
             * @param x2
             * the X coordinate for the end point of the resulting
             * {@code CubicCurve2D}
             * @param y2
             * the Y coordinate for the end point of the resulting
             * {@code CubicCurve2D}
             * @since 1.2
             */
            constructor(x1?: any, y1?: any, ctrlx1?: any, ctrly1?: any, ctrlx2?: any, ctrly2?: any, x2?: any, y2?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP1(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlX1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlY1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlP1(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlX2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlY2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlP2(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP2(): java.awt.geom.Point2D;
            /**
             * Sets the location of the end points and control points of this curve
             * to the specified {@code float} coordinates.
             *
             * @param x1
             * the X coordinate used to set the start point of this
             * {@code CubicCurve2D}
             * @param y1
             * the Y coordinate used to set the start point of this
             * {@code CubicCurve2D}
             * @param ctrlx1
             * the X coordinate used to set the first control point of
             * this {@code CubicCurve2D}
             * @param ctrly1
             * the Y coordinate used to set the first control point of
             * this {@code CubicCurve2D}
             * @param ctrlx2
             * the X coordinate used to set the second control point of
             * this {@code CubicCurve2D}
             * @param ctrly2
             * the Y coordinate used to set the second control point of
             * this {@code CubicCurve2D}
             * @param x2
             * the X coordinate used to set the end point of this
             * {@code CubicCurve2D}
             * @param y2
             * the Y coordinate used to set the end point of this
             * {@code CubicCurve2D}
             * @since 1.2
             */
            setCurve(x1?: any, y1?: any, ctrlx1?: any, ctrly1?: any, ctrlx2?: any, ctrly2?: any, x2?: any, y2?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setCurve$double$double$double$double$double$double$double$double(x1: number, y1: number, ctrlx1: number, ctrly1: number, ctrlx2: number, ctrly2: number, x2: number, y2: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
    }
}
declare namespace java.awt.geom {
    /**
     * A utility class to iterate over the path segments of a cubic curve segment
     * through the PathIterator interface.
     *
     * @author Jim Graham
     */
    class CubicIterator implements java.awt.geom.PathIterator {
        cubic: java.awt.geom.CubicCurve2D;
        affine: java.awt.geom.AffineTransform;
        index: number;
        constructor(q: java.awt.geom.CubicCurve2D, at: java.awt.geom.AffineTransform);
        /**
         * Return the winding rule for determining the insideness of the path.
         *
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if there are more points to read.
         *
         * @return true if there are more points to read
         */
        isDone(): boolean;
        /**
         * Moves the iterator to the next segment of the path forwards along the
         * primary direction of traversal as long as there are more points in that
         * direction.
         */
        next(): void;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of float x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment(coords?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of double x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>Dimension2D</code> class is to encapsulate a width and a height
     * dimension.
     * <p>
     * This class is only the abstract superclass for all objects that store a 2D
     * dimension. The actual storage representation of the sizes is left to the
     * subclass.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class Dimension2D implements java.lang.Cloneable {
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for instantiation
         * and provide a number of formats for storing the information necessary to
         * satisfy the various accessor methods below.
         *
         * @see java.awt.Dimension
         * @since 1.2
         */
        constructor();
        /**
         * Returns the width of this <code>Dimension</code> in double precision.
         *
         * @return the width of this <code>Dimension</code>.
         * @since 1.2
         */
        abstract getWidth(): number;
        /**
         * Returns the height of this <code>Dimension</code> in double precision.
         *
         * @return the height of this <code>Dimension</code>.
         * @since 1.2
         */
        abstract getHeight(): number;
        setSize(width?: any, height?: any): any;
        /**
         * Sets the size of this <code>Dimension</code> object to the specified
         * width and height. This method is included for completeness, to parallel
         * the {@link java.awt.Component#getSize getSize} method of
         * {@link java.awt.Component}.
         *
         * @param width
         * the new width for the <code>Dimension</code> object
         * @param height
         * the new height for the <code>Dimension</code> object
         * @since 1.2
         */
        setSize$double$double(width: number, height: number): void;
        /**
         * Sets the size of this <code>Dimension2D</code> object to match the
         * specified size. This method is included for completeness, to parallel the
         * <code>getSize</code> method of <code>Component</code>.
         *
         * @param d
         * the new size for the <code>Dimension2D</code> object
         * @since 1.2
         */
        setSize$java_awt_geom_Dimension2D(d: Dimension2D): void;
        /**
         * Creates a new object of the same class as this object.
         *
         * @return a clone of this instance.
         * @exception OutOfMemoryError
         * if there is not enough memory.
         * @see java.lang.Cloneable
         * @since 1.2
         */
        clone(): any;
    }
}
declare namespace java.awt.geom {
    /**
     * A utility class to iterate over the path segments of an ellipse through the
     * PathIterator interface.
     *
     * @author Jim Graham
     */
    class EllipseIterator implements java.awt.geom.PathIterator {
        x: number;
        y: number;
        w: number;
        h: number;
        affine: java.awt.geom.AffineTransform;
        index: number;
        constructor(e: java.awt.geom.Ellipse2D, at: java.awt.geom.AffineTransform);
        /**
         * Return the winding rule for determining the insideness of the path.
         *
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if there are more points to read.
         *
         * @return true if there are more points to read
         */
        isDone(): boolean;
        /**
         * Moves the iterator to the next segment of the path forwards along the
         * primary direction of traversal as long as there are more points in that
         * direction.
         */
        next(): void;
        static CtrlVal: number;
        static pcv: number;
        static pcv_$LI$(): number;
        static ncv: number;
        static ncv_$LI$(): number;
        static ctrlpts: number[][];
        static ctrlpts_$LI$(): number[][];
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of float x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment(coords?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of double x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>FlatteningPathIterator</code> class returns a flattened view of
     * another {@link PathIterator} object. Other {@link java.awt.Shape Shape}
     * classes can use this class to provide flattening behavior for their paths
     * without having to perform the interpolation calculations themselves.
     *
     * @author Jim Graham
     */
    class FlatteningPathIterator implements java.awt.geom.PathIterator {
        static GROW_SIZE: number;
        src: java.awt.geom.PathIterator;
        squareflat: number;
        limit: number;
        hold: number[];
        curx: number;
        cury: number;
        movx: number;
        movy: number;
        holdType: number;
        holdEnd: number;
        holdIndex: number;
        levels: number[];
        levelIndex: number;
        done: boolean;
        /**
         * Constructs a new <code>FlatteningPathIterator</code> object that flattens
         * a path as it iterates over it. The <code>limit</code> parameter allows
         * you to control the maximum number of recursive subdivisions that the
         * iterator can make before it assumes that the curve is flat enough without
         * measuring against the <code>flatness</code> parameter. The flattened
         * iteration therefore never generates more than a maximum of
         * <code>(2^limit)</code> line segments per curve.
         *
         * @param src
         * the original unflattened path being iterated over
         * @param flatness
         * the maximum allowable distance between the control points and
         * the flattened curve
         * @param limit
         * the maximum number of recursive subdivisions allowed for any
         * curved segment
         * @exception IllegalArgumentException
         * if <code>flatness</code> or <code>limit</code> is less
         * than zero
         */
        constructor(src: java.awt.geom.PathIterator, flatness: number, limit?: number);
        /**
         * Returns the flatness of this iterator.
         *
         * @return the flatness of this <code>FlatteningPathIterator</code>.
         */
        getFlatness(): number;
        /**
         * Returns the recursion limit of this iterator.
         *
         * @return the recursion limit of this <code>FlatteningPathIterator</code>.
         */
        getRecursionLimit(): number;
        /**
         * Returns the winding rule for determining the interior of the path.
         *
         * @return the winding rule of the original unflattened path being iterated
         * over.
         * @see PathIterator#WIND_EVEN_ODD
         * @see PathIterator#WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if the iteration is complete.
         *
         * @return <code>true</code> if all the segments have been read;
         * <code>false</code> otherwise.
         */
        isDone(): boolean;
        ensureHoldCapacity(want: number): void;
        /**
         * Moves the iterator to the next segment of the path forwards along the
         * primary direction of traversal as long as there are more points in that
         * direction.
         */
        next$(): void;
        next(doNext?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, or SEG_CLOSE. A float array of length 6 must be passed in and
         * can be used to store the coordinates of the point(s). Each point is
         * stored as a pair of float x,y coordinates. SEG_MOVETO and SEG_LINETO
         * types return one point, and SEG_CLOSE does not return any points.
         *
         * @param coords
         * an array that holds the data returned from this method
         * @return the path segment type of the current path segment.
         * @exception NoSuchElementException
         * if there are no more elements in the flattening path to be
         * returned.
         * @see PathIterator#SEG_MOVETO
         * @see PathIterator#SEG_LINETO
         * @see PathIterator#SEG_CLOSE
         */
        currentSegment(coords?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, or SEG_CLOSE. A double array of length 6 must be passed in
         * and can be used to store the coordinates of the point(s). Each point is
         * stored as a pair of double x,y coordinates. SEG_MOVETO and SEG_LINETO
         * types return one point, and SEG_CLOSE does not return any points.
         *
         * @param coords
         * an array that holds the data returned from this method
         * @return the path segment type of the current path segment.
         * @exception NoSuchElementException
         * if there are no more elements in the flattening path to be
         * returned.
         * @see PathIterator#SEG_MOVETO
         * @see PathIterator#SEG_LINETO
         * @see PathIterator#SEG_CLOSE
         */
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.awt.geom {
    /**
     * This <code>Line2D</code> represents a line segment in {@code (x,y)}
     * coordinate space. This class, like all of the Java 2D API, uses a default
     * coordinate system called <i>user space</i> in which the y-axis values
     * increase downward and x-axis values increase to the right. For more
     * information on the user space coordinate system, see the <a href=
     * "http://docs.oracle.com/javase/1.3/docs/guide/2d/spec/j2d-intro.fm2.html#61857">
     * Coordinate Systems</a> section of the Java 2D Programmer's Guide.
     * <p>
     * This class is only the abstract superclass for all objects that store a 2D
     * line segment. The actual storage representation of the coordinates is left to
     * the subclass.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class Line2D implements java.awt.Shape, java.lang.Cloneable {
        abstract getBounds2D(): any;
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for instantiation
         * and provide a number of formats for storing the information necessary to
         * satisfy the various accessory methods below.
         *
         * @see java.awt.geom.Line2D.Float
         * @see java.awt.geom.Line2D.Double
         * @since 1.2
         */
        constructor();
        /**
         * Returns the X coordinate of the start point in double precision.
         *
         * @return the X coordinate of the start point of this {@code Line2D}
         * object.
         * @since 1.2
         */
        abstract getX1(): number;
        /**
         * Returns the Y coordinate of the start point in double precision.
         *
         * @return the Y coordinate of the start point of this {@code Line2D}
         * object.
         * @since 1.2
         */
        abstract getY1(): number;
        /**
         * Returns the start <code>Point2D</code> of this <code>Line2D</code>.
         *
         * @return the start <code>Point2D</code> of this <code>Line2D</code>.
         * @since 1.2
         */
        abstract getP1(): java.awt.geom.Point2D;
        /**
         * Returns the X coordinate of the end point in double precision.
         *
         * @return the X coordinate of the end point of this {@code Line2D} object.
         * @since 1.2
         */
        abstract getX2(): number;
        /**
         * Returns the Y coordinate of the end point in double precision.
         *
         * @return the Y coordinate of the end point of this {@code Line2D} object.
         * @since 1.2
         */
        abstract getY2(): number;
        /**
         * Returns the end <code>Point2D</code> of this <code>Line2D</code>.
         *
         * @return the end <code>Point2D</code> of this <code>Line2D</code>.
         * @since 1.2
         */
        abstract getP2(): java.awt.geom.Point2D;
        /**
         * Sets the location of the end points of this <code>Line2D</code> to
         * the specified float coordinates.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @since 1.2
         */
        setLine(x1?: any, y1?: any, x2?: any, y2?: any): any;
        /**
         * Sets the location of the end points of this <code>Line2D</code> to the
         * specified double coordinates.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @since 1.2
         */
        setLine$double$double$double$double(x1: number, y1: number, x2: number, y2: number): void;
        /**
         * Sets the location of the end points of this <code>Line2D</code> to the
         * specified <code>Point2D</code> coordinates.
         *
         * @param p1
         * the start <code>Point2D</code> of the line segment
         * @param p2
         * the end <code>Point2D</code> of the line segment
         * @since 1.2
         */
        setLine$java_awt_geom_Point2D$java_awt_geom_Point2D(p1: java.awt.geom.Point2D, p2: java.awt.geom.Point2D): void;
        /**
         * Sets the location of the end points of this <code>Line2D</code> to the
         * same as those end points of the specified <code>Line2D</code>.
         *
         * @param l
         * the specified <code>Line2D</code>
         * @since 1.2
         */
        setLine$java_awt_geom_Line2D(l: Line2D): void;
        /**
         * Returns an indicator of where the specified point {@code (px,py)} lies
         * with respect to the line segment from {@code (x1,y1)} to {@code (x2,y2)}.
         * The return value can be either 1, -1, or 0 and indicates in which
         * direction the specified line must pivot around its first end point,
         * {@code (x1,y1)}, in order to point at the specified point {@code (px,py)}
         * .
         * <p>
         * A return value of 1 indicates that the line segment must turn in the
         * direction that takes the positive X axis towards the negative Y axis. In
         * the default coordinate system used by Java 2D, this direction is
         * counterclockwise.
         * <p>
         * A return value of -1 indicates that the line segment must turn in the
         * direction that takes the positive X axis towards the positive Y axis. In
         * the default coordinate system, this direction is clockwise.
         * <p>
         * A return value of 0 indicates that the point lies exactly on the line
         * segment. Note that an indicator value of 0 is rare and not useful for
         * determining collinearity because of floating point rounding issues.
         * <p>
         * If the point is colinear with the line segment, but not between the end
         * points, then the value will be -1 if the point lies
         * "beyond {@code (x1,y1)}" or 1 if the point lies "beyond {@code (x2,y2)}".
         *
         * @param x1
         * the X coordinate of the start point of the specified line
         * segment
         * @param y1
         * the Y coordinate of the start point of the specified line
         * segment
         * @param x2
         * the X coordinate of the end point of the specified line
         * segment
         * @param y2
         * the Y coordinate of the end point of the specified line
         * segment
         * @param px
         * the X coordinate of the specified point to be compared with
         * the specified line segment
         * @param py
         * the Y coordinate of the specified point to be compared with
         * the specified line segment
         * @return an integer that indicates the position of the third specified
         * coordinates with respect to the line segment formed by the first
         * two specified coordinates.
         * @since 1.2
         */
        static relativeCCW(x1: number, y1: number, x2: number, y2: number, px: number, py: number): number;
        /**
         * Returns an indicator of where the specified point {@code (px,py)} lies
         * with respect to this line segment. See the method comments of
         * {@link #relativeCCW(double, double, double, double, double, double)} to
         * interpret the return value.
         *
         * @param px
         * the X coordinate of the specified point to be compared with
         * this <code>Line2D</code>
         * @param py
         * the Y coordinate of the specified point to be compared with
         * this <code>Line2D</code>
         * @return an integer that indicates the position of the specified
         * coordinates with respect to this <code>Line2D</code>
         * @see #relativeCCW(double, double, double, double, double, double)
         * @since 1.2
         */
        relativeCCW(px?: any, py?: any): any;
        /**
         * Returns an indicator of where the specified <code>Point2D</code> lies
         * with respect to this line segment. See the method comments of
         * {@link #relativeCCW(double, double, double, double, double, double)} to
         * interpret the return value.
         *
         * @param p
         * the specified <code>Point2D</code> to be compared with this
         * <code>Line2D</code>
         * @return an integer that indicates the position of the specified
         * <code>Point2D</code> with respect to this <code>Line2D</code>
         * @see #relativeCCW(double, double, double, double, double, double)
         * @since 1.2
         */
        relativeCCW$java_awt_geom_Point2D(p: java.awt.geom.Point2D): number;
        /**
         * Tests if the line segment from {@code (x1,y1)} to {@code (x2,y2)}
         * intersects the line segment from {@code (x3,y3)} to {@code (x4,y4)}.
         *
         * @param x1
         * the X coordinate of the start point of the first specified
         * line segment
         * @param y1
         * the Y coordinate of the start point of the first specified
         * line segment
         * @param x2
         * the X coordinate of the end point of the first specified line
         * segment
         * @param y2
         * the Y coordinate of the end point of the first specified line
         * segment
         * @param x3
         * the X coordinate of the start point of the second specified
         * line segment
         * @param y3
         * the Y coordinate of the start point of the second specified
         * line segment
         * @param x4
         * the X coordinate of the end point of the second specified line
         * segment
         * @param y4
         * the Y coordinate of the end point of the second specified line
         * segment
         * @return <code>true</code> if the first specified line segment and the
         * second specified line segment intersect each other;
         * <code>false</code> otherwise.
         * @since 1.2
         */
        static linesIntersect(x1: number, y1: number, x2: number, y2: number, x3: number, y3: number, x4: number, y4: number): boolean;
        /**
         * Tests if the line segment from {@code (x1,y1)} to {@code (x2,y2)}
         * intersects this line segment.
         *
         * @param x1
         * the X coordinate of the start point of the specified line
         * segment
         * @param y1
         * the Y coordinate of the start point of the specified line
         * segment
         * @param x2
         * the X coordinate of the end point of the specified line
         * segment
         * @param y2
         * the Y coordinate of the end point of the specified line
         * segment
         * @return {@code <true>} if this line segment and the specified line
         * segment intersect each other; <code>false</code> otherwise.
         * @since 1.2
         */
        intersectsLine(x1?: any, y1?: any, x2?: any, y2?: any): any;
        /**
         * Tests if the specified line segment intersects this line segment.
         *
         * @param l
         * the specified <code>Line2D</code>
         * @return <code>true</code> if this line segment and the specified line
         * segment intersect each other; <code>false</code> otherwise.
         * @since 1.2
         */
        intersectsLine$java_awt_geom_Line2D(l: Line2D): boolean;
        /**
         * Returns the square of the distance from a point to a line segment. The
         * distance measured is the distance between the specified point and the
         * closest point between the specified end points. If the specified point
         * intersects the line segment in between the end points, this method
         * returns 0.0.
         *
         * @param x1
         * the X coordinate of the start point of the specified line
         * segment
         * @param y1
         * the Y coordinate of the start point of the specified line
         * segment
         * @param x2
         * the X coordinate of the end point of the specified line
         * segment
         * @param y2
         * the Y coordinate of the end point of the specified line
         * segment
         * @param px
         * the X coordinate of the specified point being measured against
         * the specified line segment
         * @param py
         * the Y coordinate of the specified point being measured against
         * the specified line segment
         * @return a double value that is the square of the distance from the
         * specified point to the specified line segment.
         * @see #ptLineDistSq(double, double, double, double, double, double)
         * @since 1.2
         */
        static ptSegDistSq(x1: number, y1: number, x2: number, y2: number, px: number, py: number): number;
        /**
         * Returns the distance from a point to a line segment. The distance
         * measured is the distance between the specified point and the closest
         * point between the specified end points. If the specified point intersects
         * the line segment in between the end points, this method returns 0.0.
         *
         * @param x1
         * the X coordinate of the start point of the specified line
         * segment
         * @param y1
         * the Y coordinate of the start point of the specified line
         * segment
         * @param x2
         * the X coordinate of the end point of the specified line
         * segment
         * @param y2
         * the Y coordinate of the end point of the specified line
         * segment
         * @param px
         * the X coordinate of the specified point being measured against
         * the specified line segment
         * @param py
         * the Y coordinate of the specified point being measured against
         * the specified line segment
         * @return a double value that is the distance from the specified point to
         * the specified line segment.
         * @see #ptLineDist(double, double, double, double, double, double)
         * @since 1.2
         */
        static ptSegDist(x1: number, y1: number, x2: number, y2: number, px: number, py: number): number;
        /**
         * Returns the square of the distance from a point to this line segment. The
         * distance measured is the distance between the specified point and the
         * closest point between the current line's end points. If the specified
         * point intersects the line segment in between the end points, this method
         * returns 0.0.
         *
         * @param px
         * the X coordinate of the specified point being measured against
         * this line segment
         * @param py
         * the Y coordinate of the specified point being measured against
         * this line segment
         * @return a double value that is the square of the distance from the
         * specified point to the current line segment.
         * @see #ptLineDistSq(double, double)
         * @since 1.2
         */
        ptSegDistSq(px?: any, py?: any): any;
        /**
         * Returns the square of the distance from a <code>Point2D</code> to this
         * line segment. The distance measured is the distance between the specified
         * point and the closest point between the current line's end points. If the
         * specified point intersects the line segment in between the end points,
         * this method returns 0.0.
         *
         * @param pt
         * the specified <code>Point2D</code> being measured against this
         * line segment.
         * @return a double value that is the square of the distance from the
         * specified <code>Point2D</code> to the current line segment.
         * @see #ptLineDistSq(Point2D)
         * @since 1.2
         */
        ptSegDistSq$java_awt_geom_Point2D(pt: java.awt.geom.Point2D): number;
        /**
         * Returns the distance from a point to this line segment. The distance
         * measured is the distance between the specified point and the closest
         * point between the current line's end points. If the specified point
         * intersects the line segment in between the end points, this method
         * returns 0.0.
         *
         * @param px
         * the X coordinate of the specified point being measured against
         * this line segment
         * @param py
         * the Y coordinate of the specified point being measured against
         * this line segment
         * @return a double value that is the distance from the specified point to
         * the current line segment.
         * @see #ptLineDist(double, double)
         * @since 1.2
         */
        ptSegDist(px?: any, py?: any): any;
        /**
         * Returns the distance from a <code>Point2D</code> to this line segment.
         * The distance measured is the distance between the specified point and the
         * closest point between the current line's end points. If the specified
         * point intersects the line segment in between the end points, this method
         * returns 0.0.
         *
         * @param pt
         * the specified <code>Point2D</code> being measured against this
         * line segment
         * @return a double value that is the distance from the specified
         * <code>Point2D</code> to the current line segment.
         * @see #ptLineDist(Point2D)
         * @since 1.2
         */
        ptSegDist$java_awt_geom_Point2D(pt: java.awt.geom.Point2D): number;
        /**
         * Returns the square of the distance from a point to a line. The distance
         * measured is the distance between the specified point and the closest
         * point on the infinitely-extended line defined by the specified
         * coordinates. If the specified point intersects the line, this method
         * returns 0.0.
         *
         * @param x1
         * the X coordinate of the start point of the specified line
         * @param y1
         * the Y coordinate of the start point of the specified line
         * @param x2
         * the X coordinate of the end point of the specified line
         * @param y2
         * the Y coordinate of the end point of the specified line
         * @param px
         * the X coordinate of the specified point being measured against
         * the specified line
         * @param py
         * the Y coordinate of the specified point being measured against
         * the specified line
         * @return a double value that is the square of the distance from the
         * specified point to the specified line.
         * @see #ptSegDistSq(double, double, double, double, double, double)
         * @since 1.2
         */
        static ptLineDistSq(x1: number, y1: number, x2: number, y2: number, px: number, py: number): number;
        /**
         * Returns the distance from a point to a line. The distance measured is the
         * distance between the specified point and the closest point on the
         * infinitely-extended line defined by the specified coordinates. If the
         * specified point intersects the line, this method returns 0.0.
         *
         * @param x1
         * the X coordinate of the start point of the specified line
         * @param y1
         * the Y coordinate of the start point of the specified line
         * @param x2
         * the X coordinate of the end point of the specified line
         * @param y2
         * the Y coordinate of the end point of the specified line
         * @param px
         * the X coordinate of the specified point being measured against
         * the specified line
         * @param py
         * the Y coordinate of the specified point being measured against
         * the specified line
         * @return a double value that is the distance from the specified point to
         * the specified line.
         * @see #ptSegDist(double, double, double, double, double, double)
         * @since 1.2
         */
        static ptLineDist(x1: number, y1: number, x2: number, y2: number, px: number, py: number): number;
        /**
         * Returns the square of the distance from a point to this line. The
         * distance measured is the distance between the specified point and the
         * closest point on the infinitely-extended line defined by this
         * <code>Line2D</code>. If the specified point intersects the line, this
         * method returns 0.0.
         *
         * @param px
         * the X coordinate of the specified point being measured against
         * this line
         * @param py
         * the Y coordinate of the specified point being measured against
         * this line
         * @return a double value that is the square of the distance from a
         * specified point to the current line.
         * @see #ptSegDistSq(double, double)
         * @since 1.2
         */
        ptLineDistSq(px?: any, py?: any): any;
        /**
         * Returns the square of the distance from a specified <code>Point2D</code>
         * to this line. The distance measured is the distance between the specified
         * point and the closest point on the infinitely-extended line defined by
         * this <code>Line2D</code>. If the specified point intersects the line,
         * this method returns 0.0.
         *
         * @param pt
         * the specified <code>Point2D</code> being measured against this
         * line
         * @return a double value that is the square of the distance from a
         * specified <code>Point2D</code> to the current line.
         * @see #ptSegDistSq(Point2D)
         * @since 1.2
         */
        ptLineDistSq$java_awt_geom_Point2D(pt: java.awt.geom.Point2D): number;
        /**
         * Returns the distance from a point to this line. The distance measured is
         * the distance between the specified point and the closest point on the
         * infinitely-extended line defined by this <code>Line2D</code>. If the
         * specified point intersects the line, this method returns 0.0.
         *
         * @param px
         * the X coordinate of the specified point being measured against
         * this line
         * @param py
         * the Y coordinate of the specified point being measured against
         * this line
         * @return a double value that is the distance from a specified point to the
         * current line.
         * @see #ptSegDist(double, double)
         * @since 1.2
         */
        ptLineDist(px?: any, py?: any): any;
        /**
         * Returns the distance from a <code>Point2D</code> to this line. The
         * distance measured is the distance between the specified point and the
         * closest point on the infinitely-extended line defined by this
         * <code>Line2D</code>. If the specified point intersects the line, this
         * method returns 0.0.
         *
         * @param pt
         * the specified <code>Point2D</code> being measured
         * @return a double value that is the distance from a specified
         * <code>Point2D</code> to the current line.
         * @see #ptSegDist(Point2D)
         * @since 1.2
         */
        ptLineDist$java_awt_geom_Point2D(pt: java.awt.geom.Point2D): number;
        /**
         * Tests if a specified coordinate is inside the boundary of this
         * <code>Line2D</code>. This method is required to implement the
         * {@link Shape} interface, but in the case of <code>Line2D</code> objects
         * it always returns <code>false</code> since a line contains no area.
         *
         * @param x
         * the X coordinate of the specified point to be tested
         * @param y
         * the Y coordinate of the specified point to be tested
         * @return <code>false</code> because a <code>Line2D</code> contains no
         * area.
         * @since 1.2
         */
        contains$double$double(x: number, y: number): boolean;
        /**
         * Tests if a given <code>Point2D</code> is inside the boundary of this
         * <code>Line2D</code>. This method is required to implement the
         * {@link Shape} interface, but in the case of <code>Line2D</code> objects
         * it always returns <code>false</code> since a line contains no area.
         *
         * @param p
         * the specified <code>Point2D</code> to be tested
         * @return <code>false</code> because a <code>Line2D</code> contains no
         * area.
         * @since 1.2
         */
        contains$java_awt_geom_Point2D(p: java.awt.geom.Point2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * Tests if the interior of this <code>Line2D</code> entirely contains the
         * specified set of rectangular coordinates. This method is required to
         * implement the <code>Shape</code> interface, but in the case of
         * <code>Line2D</code> objects it always returns false since a line contains
         * no area.
         *
         * @param x
         * the X coordinate of the upper-left corner of the specified
         * rectangular area
         * @param y
         * the Y coordinate of the upper-left corner of the specified
         * rectangular area
         * @param w
         * the width of the specified rectangular area
         * @param h
         * the height of the specified rectangular area
         * @return <code>false</code> because a <code>Line2D</code> contains no
         * area.
         * @since 1.2
         */
        contains(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Tests if the interior of this <code>Line2D</code> entirely contains the
         * specified <code>Rectangle2D</code>. This method is required to implement
         * the <code>Shape</code> interface, but in the case of <code>Line2D</code>
         * objects it always returns <code>false</code> since a line contains no
         * area.
         *
         * @param r
         * the specified <code>Rectangle2D</code> to be tested
         * @return <code>false</code> because a <code>Line2D</code> contains no
         * area.
         * @since 1.2
         */
        contains$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        getBounds(): java.awt.Rectangle;
        /**
         * Returns an iteration object that defines the boundary of this
         * <code>Line2D</code>. The iterator for this class is not multi-threaded
         * safe, which means that this <code>Line2D</code> class does not guarantee
         * that modifications to the geometry of this <code>Line2D</code> object do
         * not affect any iterations of that geometry that are already in process.
         *
         * @param at
         * the specified {@link AffineTransform}
         * @return a {@link PathIterator} that defines the boundary of this
         * <code>Line2D</code>.
         * @since 1.2
         */
        getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
        /**
         * Returns an iteration object that defines the boundary of this flattened
         * <code>Line2D</code>. The iterator for this class is not multi-threaded
         * safe, which means that this <code>Line2D</code> class does not guarantee
         * that modifications to the geometry of this <code>Line2D</code> object do
         * not affect any iterations of that geometry that are already in process.
         *
         * @param at
         * the specified <code>AffineTransform</code>
         * @param flatness
         * the maximum amount that the control points for a given curve
         * can vary from colinear before a subdivided curve is replaced
         * by a straight line connecting the end points. Since a
         * <code>Line2D</code> object is always flat, this parameter is
         * ignored.
         * @return a <code>PathIterator</code> that defines the boundary of the
         * flattened <code>Line2D</code>
         * @since 1.2
         */
        getPathIterator(at?: any, flatness?: any): any;
        /**
         * Creates a new object of the same class as this object.
         *
         * @return a clone of this instance.
         * @exception OutOfMemoryError
         * if there is not enough memory.
         * @see java.lang.Cloneable
         * @since 1.2
         */
        clone(): any;
    }
    namespace Line2D {
        /**
         * A line segment specified with float coordinates.
         *
         * @since 1.2
         */
        class Float extends java.awt.geom.Line2D implements java.io.Serializable {
            /**
             * The X coordinate of the start point of the line segment.
             *
             * @since 1.2
             * @serial
             */
            x1: number;
            /**
             * The Y coordinate of the start point of the line segment.
             *
             * @since 1.2
             * @serial
             */
            y1: number;
            /**
             * The X coordinate of the end point of the line segment.
             *
             * @since 1.2
             * @serial
             */
            x2: number;
            /**
             * The Y coordinate of the end point of the line segment.
             *
             * @since 1.2
             * @serial
             */
            y2: number;
            /**
             * Constructs and initializes a Line from the specified coordinates.
             *
             * @param x1
             * the X coordinate of the start point
             * @param y1
             * the Y coordinate of the start point
             * @param x2
             * the X coordinate of the end point
             * @param y2
             * the Y coordinate of the end point
             * @since 1.2
             */
            constructor(x1?: any, y1?: any, x2?: any, y2?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP1(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP2(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setLine$double$double$double$double(x1: number, y1: number, x2: number, y2: number): void;
            /**
             * Sets the location of the end points of this <code>Line2D</code> to
             * the specified float coordinates.
             *
             * @param x1
             * the X coordinate of the start point
             * @param y1
             * the Y coordinate of the start point
             * @param x2
             * the X coordinate of the end point
             * @param y2
             * the Y coordinate of the end point
             * @since 1.2
             */
            setLine(x1?: any, y1?: any, x2?: any, y2?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
        /**
         * A line segment specified with double coordinates.
         *
         * @since 1.2
         */
        class Double extends java.awt.geom.Line2D implements java.io.Serializable {
            /**
             * The X coordinate of the start point of the line segment.
             *
             * @since 1.2
             * @serial
             */
            x1: number;
            /**
             * The Y coordinate of the start point of the line segment.
             *
             * @since 1.2
             * @serial
             */
            y1: number;
            /**
             * The X coordinate of the end point of the line segment.
             *
             * @since 1.2
             * @serial
             */
            x2: number;
            /**
             * The Y coordinate of the end point of the line segment.
             *
             * @since 1.2
             * @serial
             */
            y2: number;
            /**
             * Constructs and initializes a <code>Line2D</code> from the specified
             * coordinates.
             *
             * @param x1
             * the X coordinate of the start point
             * @param y1
             * the Y coordinate of the start point
             * @param x2
             * the X coordinate of the end point
             * @param y2
             * the Y coordinate of the end point
             * @since 1.2
             */
            constructor(x1?: any, y1?: any, x2?: any, y2?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP1(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP2(): java.awt.geom.Point2D;
            /**
             * Sets the location of the end points of this <code>Line2D</code> to
             * the specified float coordinates.
             *
             * @param x1
             * the X coordinate of the start point
             * @param y1
             * the Y coordinate of the start point
             * @param x2
             * the X coordinate of the end point
             * @param y2
             * the Y coordinate of the end point
             * @since 1.2
             */
            setLine(x1?: any, y1?: any, x2?: any, y2?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setLine$double$double$double$double(x1: number, y1: number, x2: number, y2: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
    }
}
declare namespace java.awt.geom {
    /**
     * A utility class to iterate over the path segments of a line segment through
     * the PathIterator interface.
     *
     * @author Jim Graham
     */
    class LineIterator implements java.awt.geom.PathIterator {
        line: java.awt.geom.Line2D;
        affine: java.awt.geom.AffineTransform;
        index: number;
        constructor(l: java.awt.geom.Line2D, at: java.awt.geom.AffineTransform);
        /**
         * Return the winding rule for determining the insideness of the path.
         *
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if there are more points to read.
         *
         * @return true if there are more points to read
         */
        isDone(): boolean;
        next(doNext?: any): any;
        /**
         * Moves the iterator to the next segment of the path forwards along the
         * primary direction of traversal as long as there are more points in that
         * direction.
         */
        next$(): void;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of float x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment(coords?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of double x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>PathIterator</code> interface provides the mechanism for objects
     * that implement the {@link java.awt.Shape Shape} interface to return the
     * geometry of their boundary by allowing a caller to retrieve the path of that
     * boundary a segment at a time. This interface allows these objects to retrieve
     * the path of their boundary a segment at a time by using 1st through 3rd order
     * B&eacute;zier curves, which are lines and quadratic or cubic B&eacute;zier
     * splines.
     * <p>
     * Multiple subpaths can be expressed by using a "MOVETO" segment to create a
     * discontinuity in the geometry to move from the end of one subpath to the
     * beginning of the next.
     * <p>
     * Each subpath can be closed manually by ending the last segment in the subpath
     * on the same coordinate as the beginning "MOVETO" segment for that subpath or
     * by using a "CLOSE" segment to append a line segment from the last point back
     * to the first. Be aware that manually closing an outline as opposed to using a
     * "CLOSE" segment to close the path might result in different line style
     * decorations being used at the end points of the subpath. For example, the
     * {@link java.awt.BasicStroke BasicStroke} object uses a line "JOIN" decoration
     * to connect the first and last points if a "CLOSE" segment is encountered,
     * whereas simply ending the path on the same coordinate as the beginning
     * coordinate results in line "CAP" decorations being used at the ends.
     *
     * @see java.awt.Shape
     * @see java.awt.BasicStroke
     *
     * @author Jim Graham
     */
    interface PathIterator {
        /**
         * Returns the winding rule for determining the interior of the path.
         *
         * @return the winding rule.
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if the iteration is complete.
         *
         * @return <code>true</code> if all the segments have been read;
         * <code>false</code> otherwise.
         */
        isDone(): boolean;
        next(doNext?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path-segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
         * length 6 must be passed in and can be used to store the coordinates of
         * the point(s). Each point is stored as a pair of float x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types returns one point, SEG_QUADTO returns two
         * points, SEG_CUBICTO returns 3 points and SEG_CLOSE does not return any
         * points.
         *
         * @param coords
         * an array that holds the data returned from this method
         * @return the path-segment type of the current path segment.
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment(coords?: any): any;
    }
    namespace PathIterator {
        /**
         * The winding rule constant for specifying an even-odd rule for determining
         * the interior of a path. The even-odd rule specifies that a point lies
         * inside the path if a ray drawn in any direction from that point to
         * infinity is crossed by path segments an odd number of times.
         */
        var WIND_EVEN_ODD: number;
        /**
         * The winding rule constant for specifying a non-zero rule for determining
         * the interior of a path. The non-zero rule specifies that a point lies
         * inside the path if a ray drawn in any direction from that point to
         * infinity is crossed by path segments a different number of times in the
         * counter-clockwise direction than the clockwise direction.
         */
        var WIND_NON_ZERO: number;
        /**
         * The segment type constant for a point that specifies the starting
         * location for a new subpath.
         */
        var SEG_MOVETO: number;
        /**
         * The segment type constant for a point that specifies the end point of a
         * line to be drawn from the most recently specified point.
         */
        var SEG_LINETO: number;
        /**
         * The segment type constant for the pair of points that specify a quadratic
         * parametric curve to be drawn from the most recently specified point. The
         * curve is interpolated by solving the parametric control equation in the
         * range <code>(t=[0..1])</code> using the most recently specified (current)
         * point (CP), the first control point (P1), and the final interpolated
         * control point (P2). The parametric control equation for this curve is:
         *
         * <pre>
         * P(t) = B(2,0)*CP + B(2,1)*P1 + B(2,2)*P2
         * 0 &lt;= t &lt;= 1
         *
         * B(n,m) = mth coefficient of nth degree Bernstein polynomial
         * = C(n,m) * t^(m) * (1 - t)^(n-m)
         * C(n,m) = Combinations of n things, taken m at a time
         * = n! / (m! * (n-m)!)
         * </pre>
         */
        var SEG_QUADTO: number;
        /**
         * The segment type constant for the set of 3 points that specify a cubic
         * parametric curve to be drawn from the most recently specified point. The
         * curve is interpolated by solving the parametric control equation in the
         * range <code>(t=[0..1])</code> using the most recently specified (current)
         * point (CP), the first control point (P1), the second control point (P2),
         * and the final interpolated control point (P3). The parametric control
         * equation for this curve is:
         *
         * <pre>
         * P(t) = B(3,0)*CP + B(3,1)*P1 + B(3,2)*P2 + B(3,3)*P3
         * 0 &lt;= t &lt;= 1
         *
         * B(n,m) = mth coefficient of nth degree Bernstein polynomial
         * = C(n,m) * t^(m) * (1 - t)^(n-m)
         * C(n,m) = Combinations of n things, taken m at a time
         * = n! / (m! * (n-m)!)
         * </pre>
         *
         * This form of curve is commonly known as a B&eacute;zier curve.
         */
        var SEG_CUBICTO: number;
        /**
         * The segment type constant that specifies that the preceding subpath
         * should be closed by appending a line segment back to the point
         * corresponding to the most recent SEG_MOVETO.
         */
        var SEG_CLOSE: number;
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>Point2D</code> class defines a point representing a location in
     * {@code (x,y)} coordinate space.
     * <p>
     * This class is only the abstract superclass for all objects that store a 2D
     * coordinate. The actual storage representation of the coordinates is left to
     * the subclass.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class Point2D implements java.lang.Cloneable {
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for instantiation
         * and provide a number of formats for storing the information necessary to
         * satisfy the various accessor methods below.
         *
         * @see java.awt.geom.Point2D.Float
         * @see java.awt.geom.Point2D.Double
         * @see java.awt.Point
         * @since 1.2
         */
        constructor();
        /**
         * Returns the X coordinate of this <code>Point2D</code> in
         * <code>double</code> precision.
         *
         * @return the X coordinate of this <code>Point2D</code>.
         * @since 1.2
         */
        abstract getX(): number;
        /**
         * Returns the Y coordinate of this <code>Point2D</code> in
         * <code>double</code> precision.
         *
         * @return the Y coordinate of this <code>Point2D</code>.
         * @since 1.2
         */
        abstract getY(): number;
        setLocation(x?: any, y?: any): any;
        /**
         * Sets the location of this <code>Point2D</code> to the specified
         * <code>double</code> coordinates.
         *
         * @param x
         * the new X coordinate of this {@code Point2D}
         * @param y
         * the new Y coordinate of this {@code Point2D}
         * @since 1.2
         */
        setLocation$double$double(x: number, y: number): void;
        /**
         * Sets the location of this <code>Point2D</code> to the same coordinates as
         * the specified <code>Point2D</code> object.
         *
         * @param p
         * the specified <code>Point2D</code> to which to set this
         * <code>Point2D</code>
         * @since 1.2
         */
        setLocation$java_awt_geom_Point2D(p: Point2D): void;
        /**
         * Returns the square of the distance between two points.
         *
         * @param x1
         * the X coordinate of the first specified point
         * @param y1
         * the Y coordinate of the first specified point
         * @param x2
         * the X coordinate of the second specified point
         * @param y2
         * the Y coordinate of the second specified point
         * @return the square of the distance between the two sets of specified
         * coordinates.
         * @since 1.2
         */
        static distanceSq(x1: number, y1: number, x2: number, y2: number): number;
        /**
         * Returns the distance between two points.
         *
         * @param x1
         * the X coordinate of the first specified point
         * @param y1
         * the Y coordinate of the first specified point
         * @param x2
         * the X coordinate of the second specified point
         * @param y2
         * the Y coordinate of the second specified point
         * @return the distance between the two sets of specified coordinates.
         * @since 1.2
         */
        static distance(x1: number, y1: number, x2: number, y2: number): number;
        /**
         * Returns the square of the distance from this <code>Point2D</code> to a
         * specified point.
         *
         * @param px
         * the X coordinate of the specified point to be measured against
         * this <code>Point2D</code>
         * @param py
         * the Y coordinate of the specified point to be measured against
         * this <code>Point2D</code>
         * @return the square of the distance between this <code>Point2D</code> and
         * the specified point.
         * @since 1.2
         */
        distanceSq(px?: any, py?: any): any;
        /**
         * Returns the square of the distance from this <code>Point2D</code> to a
         * specified <code>Point2D</code>.
         *
         * @param pt
         * the specified point to be measured against this
         * <code>Point2D</code>
         * @return the square of the distance between this <code>Point2D</code> to a
         * specified <code>Point2D</code>.
         * @since 1.2
         */
        distanceSq$java_awt_geom_Point2D(pt: Point2D): number;
        /**
         * Returns the distance from this <code>Point2D</code> to a specified point.
         *
         * @param px
         * the X coordinate of the specified point to be measured against
         * this <code>Point2D</code>
         * @param py
         * the Y coordinate of the specified point to be measured against
         * this <code>Point2D</code>
         * @return the distance between this <code>Point2D</code> and a specified
         * point.
         * @since 1.2
         */
        distance(px?: any, py?: any): any;
        /**
         * Returns the distance from this <code>Point2D</code> to a specified
         * <code>Point2D</code>.
         *
         * @param pt
         * the specified point to be measured against this
         * <code>Point2D</code>
         * @return the distance between this <code>Point2D</code> and the specified
         * <code>Point2D</code>.
         * @since 1.2
         */
        distance$java_awt_geom_Point2D(pt: Point2D): number;
        /**
         * Creates a new object of the same class and with the same contents as this
         * object.
         *
         * @return a clone of this instance.
         * @exception OutOfMemoryError
         * if there is not enough memory.
         * @see java.lang.Cloneable
         * @since 1.2
         */
        clone(): any;
        /**
         * Returns the hashcode for this <code>Point2D</code>.
         *
         * @return a hash code for this <code>Point2D</code>.
         */
        hashCode(): number;
        /**
         * Determines whether or not two points are equal. Two instances of
         * <code>Point2D</code> are equal if the values of their <code>x</code> and
         * <code>y</code> member fields, representing their position in the
         * coordinate space, are the same.
         *
         * @param obj
         * an object to be compared with this <code>Point2D</code>
         * @return <code>true</code> if the object to be compared is an instance of
         * <code>Point2D</code> and has the same values; <code>false</code>
         * otherwise.
         * @since 1.2
         */
        equals(obj: any): boolean;
    }
    namespace Point2D {
        /**
         * The <code>Float</code> class defines a point specified in float
         * precision.
         *
         * @since 1.2
         */
        class Float extends java.awt.geom.Point2D implements java.io.Serializable {
            /**
             * The X coordinate of this <code>Point2D</code>.
             *
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of this <code>Point2D</code>.
             *
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * Constructs and initializes a <code>Point2D</code> with the specified
             * coordinates.
             *
             * @param x
             * the X coordinate of the newly constructed
             * <code>Point2D</code>
             * @param y
             * the Y coordinate of the newly constructed
             * <code>Point2D</code>
             * @since 1.2
             */
            constructor(x?: any, y?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setLocation$double$double(x: number, y: number): void;
            /**
             * Sets the location of this <code>Point2D</code> to the specified
             * <code>float</code> coordinates.
             *
             * @param x
             * the new X coordinate of this {@code Point2D}
             * @param y
             * the new Y coordinate of this {@code Point2D}
             * @since 1.2
             */
            setLocation(x?: any, y?: any): any;
            /**
             * Returns a <code>String</code> that represents the value of this
             * <code>Point2D</code>.
             *
             * @return a string representation of this <code>Point2D</code>.
             * @since 1.2
             */
            toString(): string;
            static serialVersionUID: number;
        }
        /**
         * The <code>Double</code> class defines a point specified in
         * <code>double</code> precision.
         *
         * @since 1.2
         */
        class Double extends java.awt.geom.Point2D implements java.io.Serializable {
            /**
             * The X coordinate of this <code>Point2D</code>.
             *
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of this <code>Point2D</code>.
             *
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * Constructs and initializes a <code>Point2D</code> with the specified
             * coordinates.
             *
             * @param x
             * the X coordinate of the newly constructed
             * <code>Point2D</code>
             * @param y
             * the Y coordinate of the newly constructed
             * <code>Point2D</code>
             * @since 1.2
             */
            constructor(x?: any, y?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * Sets the location of this <code>Point2D</code> to the specified
             * <code>float</code> coordinates.
             *
             * @param x
             * the new X coordinate of this {@code Point2D}
             * @param y
             * the new Y coordinate of this {@code Point2D}
             * @since 1.2
             */
            setLocation(x?: any, y?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setLocation$double$double(x: number, y: number): void;
            /**
             * Returns a <code>String</code> that represents the value of this
             * <code>Point2D</code>.
             *
             * @return a string representation of this <code>Point2D</code>.
             * @since 1.2
             */
            toString(): string;
            static serialVersionUID: number;
        }
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>QuadCurve2D</code> class defines a quadratic parametric curve
     * segment in {@code (x,y)} coordinate space.
     * <p>
     * This class is only the abstract superclass for all objects that store a 2D
     * quadratic curve segment. The actual storage representation of the coordinates
     * is left to the subclass.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class QuadCurve2D implements java.awt.Shape, java.lang.Cloneable {
        abstract getBounds2D(): any;
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for instantiation
         * and provide a number of formats for storing the information necessary to
         * satisfy the various accessor methods below.
         *
         * @see java.awt.geom.QuadCurve2D.Float
         * @see java.awt.geom.QuadCurve2D.Double
         * @since 1.2
         */
        constructor();
        /**
         * Returns the X coordinate of the start point in <code>double</code> in
         * precision.
         *
         * @return the X coordinate of the start point.
         * @since 1.2
         */
        abstract getX1(): number;
        /**
         * Returns the Y coordinate of the start point in <code>double</code>
         * precision.
         *
         * @return the Y coordinate of the start point.
         * @since 1.2
         */
        abstract getY1(): number;
        /**
         * Returns the start point.
         *
         * @return a <code>Point2D</code> that is the start point of this
         * <code>QuadCurve2D</code>.
         * @since 1.2
         */
        abstract getP1(): java.awt.geom.Point2D;
        /**
         * Returns the X coordinate of the control point in <code>double</code>
         * precision.
         *
         * @return X coordinate the control point
         * @since 1.2
         */
        abstract getCtrlX(): number;
        /**
         * Returns the Y coordinate of the control point in <code>double</code>
         * precision.
         *
         * @return the Y coordinate of the control point.
         * @since 1.2
         */
        abstract getCtrlY(): number;
        /**
         * Returns the control point.
         *
         * @return a <code>Point2D</code> that is the control point of this
         * <code>Point2D</code>.
         * @since 1.2
         */
        abstract getCtrlPt(): java.awt.geom.Point2D;
        /**
         * Returns the X coordinate of the end point in <code>double</code>
         * precision.
         *
         * @return the x coordinate of the end point.
         * @since 1.2
         */
        abstract getX2(): number;
        /**
         * Returns the Y coordinate of the end point in <code>double</code>
         * precision.
         *
         * @return the Y coordinate of the end point.
         * @since 1.2
         */
        abstract getY2(): number;
        /**
         * Returns the end point.
         *
         * @return a <code>Point</code> object that is the end point of this
         * <code>Point2D</code>.
         * @since 1.2
         */
        abstract getP2(): java.awt.geom.Point2D;
        /**
         * Sets the location of the end points and control point of this curve
         * to the specified {@code float} coordinates.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param ctrlx
         * the X coordinate of the control point
         * @param ctrly
         * the Y coordinate of the control point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @since 1.2
         */
        setCurve(x1?: any, y1?: any, ctrlx?: any, ctrly?: any, x2?: any, y2?: any): any;
        /**
         * Sets the location of the end points and control point of this curve to
         * the specified <code>double</code> coordinates.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param ctrlx
         * the X coordinate of the control point
         * @param ctrly
         * the Y coordinate of the control point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @since 1.2
         */
        setCurve$double$double$double$double$double$double(x1: number, y1: number, ctrlx: number, ctrly: number, x2: number, y2: number): void;
        /**
         * Sets the location of the end points and control points of this
         * <code>QuadCurve2D</code> to the <code>double</code> coordinates at the
         * specified offset in the specified array.
         *
         * @param coords
         * the array containing coordinate values
         * @param offset
         * the index into the array from which to start getting the
         * coordinate values and assigning them to this
         * <code>QuadCurve2D</code>
         * @since 1.2
         */
        setCurve$double_A$int(coords: number[], offset: number): void;
        /**
         * Sets the location of the end points and control point of this
         * <code>QuadCurve2D</code> to the specified <code>Point2D</code>
         * coordinates.
         *
         * @param p1
         * the start point
         * @param cp
         * the control point
         * @param p2
         * the end point
         * @since 1.2
         */
        setCurve$java_awt_geom_Point2D$java_awt_geom_Point2D$java_awt_geom_Point2D(p1: java.awt.geom.Point2D, cp: java.awt.geom.Point2D, p2: java.awt.geom.Point2D): void;
        /**
         * Sets the location of the end points and control points of this
         * <code>QuadCurve2D</code> to the coordinates of the <code>Point2D</code>
         * objects at the specified offset in the specified array.
         *
         * @param pts
         * an array containing <code>Point2D</code> that define
         * coordinate values
         * @param offset
         * the index into <code>pts</code> from which to start getting
         * the coordinate values and assigning them to this
         * <code>QuadCurve2D</code>
         * @since 1.2
         */
        setCurve$java_awt_geom_Point2D_A$int(pts: java.awt.geom.Point2D[], offset: number): void;
        /**
         * Sets the location of the end points and control point of this
         * <code>QuadCurve2D</code> to the same as those in the specified
         * <code>QuadCurve2D</code>.
         *
         * @param c
         * the specified <code>QuadCurve2D</code>
         * @since 1.2
         */
        setCurve$java_awt_geom_QuadCurve2D(c: QuadCurve2D): void;
        /**
         * Returns the square of the flatness, or maximum distance of a control
         * point from the line connecting the end points, of the quadratic curve
         * specified by the indicated control points.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param ctrlx
         * the X coordinate of the control point
         * @param ctrly
         * the Y coordinate of the control point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @return the square of the flatness of the quadratic curve defined by the
         * specified coordinates.
         * @since 1.2
         */
        static getFlatnessSq(x1?: any, y1?: any, ctrlx?: any, ctrly?: any, x2?: any, y2?: any): any;
        /**
         * Returns the flatness, or maximum distance of a control point from the
         * line connecting the end points, of the quadratic curve specified by the
         * indicated control points.
         *
         * @param x1
         * the X coordinate of the start point
         * @param y1
         * the Y coordinate of the start point
         * @param ctrlx
         * the X coordinate of the control point
         * @param ctrly
         * the Y coordinate of the control point
         * @param x2
         * the X coordinate of the end point
         * @param y2
         * the Y coordinate of the end point
         * @return the flatness of the quadratic curve defined by the specified
         * coordinates.
         * @since 1.2
         */
        static getFlatness(x1?: any, y1?: any, ctrlx?: any, ctrly?: any, x2?: any, y2?: any): any;
        /**
         * Returns the square of the flatness, or maximum distance of a control
         * point from the line connecting the end points, of the quadratic curve
         * specified by the control points stored in the indicated array at the
         * indicated index.
         *
         * @param coords
         * an array containing coordinate values
         * @param offset
         * the index into <code>coords</code> from which to to start
         * getting the values from the array
         * @return the flatness of the quadratic curve that is defined by the values
         * in the specified array at the specified index.
         * @since 1.2
         */
        static getFlatnessSq$double_A$int(coords: number[], offset: number): number;
        /**
         * Returns the flatness, or maximum distance of a control point from the
         * line connecting the end points, of the quadratic curve specified by the
         * control points stored in the indicated array at the indicated index.
         *
         * @param coords
         * an array containing coordinate values
         * @param offset
         * the index into <code>coords</code> from which to start getting
         * the coordinate values
         * @return the flatness of a quadratic curve defined by the specified array
         * at the specified offset.
         * @since 1.2
         */
        static getFlatness$double_A$int(coords: number[], offset: number): number;
        /**
         * Returns the square of the flatness, or maximum distance of a control
         * point from the line connecting the end points, of this
         * <code>QuadCurve2D</code>.
         *
         * @return the square of the flatness of this <code>QuadCurve2D</code>.
         * @since 1.2
         */
        getFlatnessSq(): number;
        /**
         * Returns the flatness, or maximum distance of a control point from the
         * line connecting the end points, of this <code>QuadCurve2D</code>.
         *
         * @return the flatness of this <code>QuadCurve2D</code>.
         * @since 1.2
         */
        getFlatness(): number;
        /**
         * Subdivides this <code>QuadCurve2D</code> and stores the resulting two
         * subdivided curves into the <code>left</code> and <code>right</code> curve
         * parameters. Either or both of the <code>left</code> and
         * <code>right</code> objects can be the same as this
         * <code>QuadCurve2D</code> or <code>null</code>.
         *
         * @param left
         * the <code>QuadCurve2D</code> object for storing the left or
         * first half of the subdivided curve
         * @param right
         * the <code>QuadCurve2D</code> object for storing the right or
         * second half of the subdivided curve
         * @since 1.2
         */
        subdivide(left: QuadCurve2D, right: QuadCurve2D): void;
        /**
         * Subdivides the quadratic curve specified by the <code>src</code>
         * parameter and stores the resulting two subdivided curves into the
         * <code>left</code> and <code>right</code> curve parameters. Either or both
         * of the <code>left</code> and <code>right</code> objects can be the same
         * as the <code>src</code> object or <code>null</code>.
         *
         * @param src
         * the quadratic curve to be subdivided
         * @param left
         * the <code>QuadCurve2D</code> object for storing the left or
         * first half of the subdivided curve
         * @param right
         * the <code>QuadCurve2D</code> object for storing the right or
         * second half of the subdivided curve
         * @since 1.2
         */
        static subdivide$java_awt_geom_QuadCurve2D$java_awt_geom_QuadCurve2D$java_awt_geom_QuadCurve2D(src: QuadCurve2D, left: QuadCurve2D, right: QuadCurve2D): void;
        /**
         * Subdivides the quadratic curve specified by the coordinates stored in the
         * <code>src</code> array at indices <code>srcoff</code> through
         * <code>srcoff</code>&nbsp;+&nbsp;5 and stores the resulting two subdivided
         * curves into the two result arrays at the corresponding indices. Either or
         * both of the <code>left</code> and <code>right</code> arrays can be
         * <code>null</code> or a reference to the same array and offset as the
         * <code>src</code> array. Note that the last point in the first subdivided
         * curve is the same as the first point in the second subdivided curve.
         * Thus, it is possible to pass the same array for <code>left</code> and
         * <code>right</code> and to use offsets such that <code>rightoff</code>
         * equals <code>leftoff</code> + 4 in order to avoid allocating extra
         * storage for this common point.
         *
         * @param src
         * the array holding the coordinates for the source curve
         * @param srcoff
         * the offset into the array of the beginning of the the 6 source
         * coordinates
         * @param left
         * the array for storing the coordinates for the first half of
         * the subdivided curve
         * @param leftoff
         * the offset into the array of the beginning of the the 6 left
         * coordinates
         * @param right
         * the array for storing the coordinates for the second half of
         * the subdivided curve
         * @param rightoff
         * the offset into the array of the beginning of the the 6 right
         * coordinates
         * @since 1.2
         */
        static subdivide(src?: any, srcoff?: any, left?: any, leftoff?: any, right?: any, rightoff?: any): any;
        /**
         * Solves the quadratic whose coefficients are in the <code>eqn</code> array
         * and places the non-complex roots back into the same array, returning the
         * number of roots. The quadratic solved is represented by the equation:
         *
         * <pre>
         * eqn = {C, B, A};
         * ax^2 + bx + c = 0
         * </pre>
         *
         * A return value of <code>-1</code> is used to distinguish a constant
         * equation, which might be always 0 or never 0, from an equation that has
         * no zeroes.
         *
         * @param eqn
         * the array that contains the quadratic coefficients
         * @return the number of roots, or <code>-1</code> if the equation is a
         * constant
         * @since 1.2
         */
        static solveQuadratic$double_A(eqn: number[]): number;
        /**
         * Solves the quadratic whose coefficients are in the <code>eqn</code> array
         * and places the non-complex roots into the <code>res</code> array,
         * returning the number of roots. The quadratic solved is represented by the
         * equation:
         *
         * <pre>
         * eqn = {C, B, A};
         * ax^2 + bx + c = 0
         * </pre>
         *
         * A return value of <code>-1</code> is used to distinguish a constant
         * equation, which might be always 0 or never 0, from an equation that has
         * no zeroes.
         *
         * @param eqn
         * the specified array of coefficients to use to solve the
         * quadratic equation
         * @param res
         * the array that contains the non-complex roots resulting from
         * the solution of the quadratic equation
         * @return the number of roots, or <code>-1</code> if the equation is a
         * constant.
         * @since 1.3
         */
        static solveQuadratic(eqn?: any, res?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double(x: number, y: number): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$java_awt_geom_Point2D(p: java.awt.geom.Point2D): boolean;
        /**
         * Fill an array with the coefficients of the parametric equation in t,
         * ready for solving against val with solveQuadratic. We currently have: val
         * = Py(t) = C1*(1-t)^2 + 2*CP*t*(1-t) + C2*t^2 = C1 - 2*C1*t + C1*t^2 +
         * 2*CP*t - 2*CP*t^2 + C2*t^2 = C1 + (2*CP - 2*C1)*t + (C1 - 2*CP + C2)*t^2
         * 0 = (C1 - val) + (2*CP - 2*C1)*t + (C1 - 2*CP + C2)*t^2 0 = C + Bt + At^2
         * C = C1 - val B = 2*CP - 2*C1 A = C1 - 2*CP + C2
         */
        static fillEqn(eqn: number[], val: number, c1: number, cp: number, c2: number): void;
        /**
         * Evaluate the t values in the first num slots of the vals[] array and
         * place the evaluated values back into the same array. Only evaluate t
         * values that are within the range &lt;0, 1&gt;, including the 0 and 1 ends
         * of the range iff the include0 or include1 booleans are true. If an
         * "inflection" equation is handed in, then any points which represent a
         * point of inflection for that quadratic equation are also ignored.
         */
        static evalQuadratic(vals: number[], num: number, include0: boolean, include1: boolean, inflect: number[], c1: number, ctrl: number, c2: number): number;
        static BELOW: number;
        static LOWEDGE: number;
        static INSIDE: number;
        static HIGHEDGE: number;
        static ABOVE: number;
        /**
         * Determine where coord lies with respect to the range from low to high. It
         * is assumed that low &lt;= high. The return value is one of the 5 values
         * BELOW, LOWEDGE, INSIDE, HIGHEDGE, or ABOVE.
         */
        static getTag(coord: number, low: number, high: number): number;
        /**
         * Determine if the pttag represents a coordinate that is already in its
         * test range, or is on the border with either of the two opttags
         * representing another coordinate that is "towards the inside" of that test
         * range. In other words, are either of the two "opt" points
         * "drawing the pt inward"?
         */
        static inwards(pttag: number, opt1tag: number, opt2tag: number): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        getBounds(): java.awt.Rectangle;
        /**
         * Returns an iteration object that defines the boundary of the shape of
         * this <code>QuadCurve2D</code>. The iterator for this class is not
         * multi-threaded safe, which means that this <code>QuadCurve2D</code> class
         * does not guarantee that modifications to the geometry of this
         * <code>QuadCurve2D</code> object do not affect any iterations of that
         * geometry that are already in process.
         *
         * @param at
         * an optional {@link AffineTransform} to apply to the shape
         * boundary
         * @return a {@link PathIterator} object that defines the boundary of the
         * shape.
         * @since 1.2
         */
        getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
        /**
         * Returns an iteration object that defines the boundary of the flattened
         * shape of this <code>QuadCurve2D</code>. The iterator for this class is
         * not multi-threaded safe, which means that this <code>QuadCurve2D</code>
         * class does not guarantee that modifications to the geometry of this
         * <code>QuadCurve2D</code> object do not affect any iterations of that
         * geometry that are already in process.
         *
         * @param at
         * an optional <code>AffineTransform</code> to apply to the
         * boundary of the shape
         * @param flatness
         * the maximum distance that the control points for a subdivided
         * curve can be with respect to a line connecting the end points
         * of this curve before this curve is replaced by a straight line
         * connecting the end points.
         * @return a <code>PathIterator</code> object that defines the flattened
         * boundary of the shape.
         * @since 1.2
         */
        getPathIterator(at?: any, flatness?: any): any;
        /**
         * Creates a new object of the same class and with the same contents as this
         * object.
         *
         * @return a clone of this instance.
         * @exception OutOfMemoryError
         * if there is not enough memory.
         * @see java.lang.Cloneable
         * @since 1.2
         */
        clone(): any;
    }
    namespace QuadCurve2D {
        /**
         * A quadratic parametric curve segment specified with {@code float}
         * coordinates.
         *
         * @since 1.2
         */
        class Float extends java.awt.geom.QuadCurve2D implements java.io.Serializable {
            /**
             * The X coordinate of the start point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            x1: number;
            /**
             * The Y coordinate of the start point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            y1: number;
            /**
             * The X coordinate of the control point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            ctrlx: number;
            /**
             * The Y coordinate of the control point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            ctrly: number;
            /**
             * The X coordinate of the end point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            x2: number;
            /**
             * The Y coordinate of the end point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            y2: number;
            /**
             * Constructs and initializes a <code>QuadCurve2D</code> from the
             * specified {@code float} coordinates.
             *
             * @param x1
             * the X coordinate of the start point
             * @param y1
             * the Y coordinate of the start point
             * @param ctrlx
             * the X coordinate of the control point
             * @param ctrly
             * the Y coordinate of the control point
             * @param x2
             * the X coordinate of the end point
             * @param y2
             * the Y coordinate of the end point
             * @since 1.2
             */
            constructor(x1?: any, y1?: any, ctrlx?: any, ctrly?: any, x2?: any, y2?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP1(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlPt(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP2(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setCurve$double$double$double$double$double$double(x1: number, y1: number, ctrlx: number, ctrly: number, x2: number, y2: number): void;
            /**
             * Sets the location of the end points and control point of this curve
             * to the specified {@code float} coordinates.
             *
             * @param x1
             * the X coordinate of the start point
             * @param y1
             * the Y coordinate of the start point
             * @param ctrlx
             * the X coordinate of the control point
             * @param ctrly
             * the Y coordinate of the control point
             * @param x2
             * the X coordinate of the end point
             * @param y2
             * the Y coordinate of the end point
             * @since 1.2
             */
            setCurve(x1?: any, y1?: any, ctrlx?: any, ctrly?: any, x2?: any, y2?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
        /**
         * A quadratic parametric curve segment specified with {@code double}
         * coordinates.
         *
         * @since 1.2
         */
        class Double extends java.awt.geom.QuadCurve2D implements java.io.Serializable {
            /**
             * The X coordinate of the start point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            x1: number;
            /**
             * The Y coordinate of the start point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            y1: number;
            /**
             * The X coordinate of the control point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            ctrlx: number;
            /**
             * The Y coordinate of the control point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            ctrly: number;
            /**
             * The X coordinate of the end point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            x2: number;
            /**
             * The Y coordinate of the end point of the quadratic curve segment.
             *
             * @since 1.2
             * @serial
             */
            y2: number;
            /**
             * Constructs and initializes a <code>QuadCurve2D</code> from the
             * specified {@code double} coordinates.
             *
             * @param x1
             * the X coordinate of the start point
             * @param y1
             * the Y coordinate of the start point
             * @param ctrlx
             * the X coordinate of the control point
             * @param ctrly
             * the Y coordinate of the control point
             * @param x2
             * the X coordinate of the end point
             * @param y2
             * the Y coordinate of the end point
             * @since 1.2
             */
            constructor(x1?: any, y1?: any, ctrlx?: any, ctrly?: any, x2?: any, y2?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY1(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP1(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getCtrlPt(): java.awt.geom.Point2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY2(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getP2(): java.awt.geom.Point2D;
            /**
             * Sets the location of the end points and control point of this curve
             * to the specified {@code float} coordinates.
             *
             * @param x1
             * the X coordinate of the start point
             * @param y1
             * the Y coordinate of the start point
             * @param ctrlx
             * the X coordinate of the control point
             * @param ctrly
             * the Y coordinate of the control point
             * @param x2
             * the X coordinate of the end point
             * @param y2
             * the Y coordinate of the end point
             * @since 1.2
             */
            setCurve(x1?: any, y1?: any, ctrlx?: any, ctrly?: any, x2?: any, y2?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setCurve$double$double$double$double$double$double(x1: number, y1: number, ctrlx: number, ctrly: number, x2: number, y2: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
    }
}
declare namespace java.awt.geom {
    /**
     * A utility class to iterate over the path segments of a quadratic curve
     * segment through the PathIterator interface.
     *
     * @author Jim Graham
     */
    class QuadIterator implements java.awt.geom.PathIterator {
        quad: java.awt.geom.QuadCurve2D;
        affine: java.awt.geom.AffineTransform;
        index: number;
        constructor(q: java.awt.geom.QuadCurve2D, at: java.awt.geom.AffineTransform);
        /**
         * Return the winding rule for determining the insideness of the path.
         *
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if there are more points to read.
         *
         * @return true if there are more points to read
         */
        isDone(): boolean;
        next(doNext?: any): any;
        /**
         * Moves the iterator to the next segment of the path forwards along the
         * primary direction of traversal as long as there are more points in that
         * direction.
         */
        next$(): void;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of float x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment(coords?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of double x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.awt.geom {
    /**
     * <code>RectangularShape</code> is the base class for a number of {@link Shape}
     * objects whose geometry is defined by a rectangular frame. This class does not
     * directly specify any specific geometry by itself, but merely provides
     * manipulation methods inherited by a whole category of <code>Shape</code>
     * objects. The manipulation methods provided by this class can be used to query
     * and modify the rectangular frame, which provides a reference for the
     * subclasses to define their geometry.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class RectangularShape implements java.awt.Shape, java.lang.Cloneable {
        abstract getBounds2D(): any;
        abstract intersects(x: any, y: any, w: any, h: any): any;
        /**
         * This is an abstract class that cannot be instantiated directly.
         *
         * @see Arc2D
         * @see Ellipse2D
         * @see Rectangle2D
         * @see RoundRectangle2D
         * @since 1.2
         */
        constructor();
        /**
         * Returns the X coordinate of the upper-left corner of the framing
         * rectangle in <code>double</code> precision.
         *
         * @return the X coordinate of the upper-left corner of the framing
         * rectangle.
         * @since 1.2
         */
        abstract getX(): number;
        /**
         * Returns the Y coordinate of the upper-left corner of the framing
         * rectangle in <code>double</code> precision.
         *
         * @return the Y coordinate of the upper-left corner of the framing
         * rectangle.
         * @since 1.2
         */
        abstract getY(): number;
        /**
         * Returns the width of the framing rectangle in <code>double</code>
         * precision.
         *
         * @return the width of the framing rectangle.
         * @since 1.2
         */
        abstract getWidth(): number;
        /**
         * Returns the height of the framing rectangle in <code>double</code>
         * precision.
         *
         * @return the height of the framing rectangle.
         * @since 1.2
         */
        abstract getHeight(): number;
        /**
         * Returns the smallest X coordinate of the framing rectangle of the
         * <code>Shape</code> in <code>double</code> precision.
         *
         * @return the smallest X coordinate of the framing rectangle of the
         * <code>Shape</code>.
         * @since 1.2
         */
        getMinX(): number;
        /**
         * Returns the smallest Y coordinate of the framing rectangle of the
         * <code>Shape</code> in <code>double</code> precision.
         *
         * @return the smallest Y coordinate of the framing rectangle of the
         * <code>Shape</code>.
         * @since 1.2
         */
        getMinY(): number;
        /**
         * Returns the largest X coordinate of the framing rectangle of the
         * <code>Shape</code> in <code>double</code> precision.
         *
         * @return the largest X coordinate of the framing rectangle of the
         * <code>Shape</code>.
         * @since 1.2
         */
        getMaxX(): number;
        /**
         * Returns the largest Y coordinate of the framing rectangle of the
         * <code>Shape</code> in <code>double</code> precision.
         *
         * @return the largest Y coordinate of the framing rectangle of the
         * <code>Shape</code>.
         * @since 1.2
         */
        getMaxY(): number;
        /**
         * Returns the X coordinate of the center of the framing rectangle of the
         * <code>Shape</code> in <code>double</code> precision.
         *
         * @return the X coordinate of the center of the framing rectangle of the
         * <code>Shape</code>.
         * @since 1.2
         */
        getCenterX(): number;
        /**
         * Returns the Y coordinate of the center of the framing rectangle of the
         * <code>Shape</code> in <code>double</code> precision.
         *
         * @return the Y coordinate of the center of the framing rectangle of the
         * <code>Shape</code>.
         * @since 1.2
         */
        getCenterY(): number;
        /**
         * Returns the framing {@link Rectangle2D} that defines the overall shape of
         * this object.
         *
         * @return a <code>Rectangle2D</code>, specified in <code>double</code>
         * coordinates.
         * @see #setFrame(double, double, double, double)
         * @see #setFrame(Point2D, Dimension2D)
         * @see #setFrame(Rectangle2D)
         * @since 1.2
         */
        getFrame(): java.awt.geom.Rectangle2D;
        /**
         * Determines whether the <code>RectangularShape</code> is empty. When the
         * <code>RectangularShape</code> is empty, it encloses no area.
         *
         * @return <code>true</code> if the <code>RectangularShape</code> is empty;
         * <code>false</code> otherwise.
         * @since 1.2
         */
        abstract isEmpty(): boolean;
        setFrame(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Sets the location and size of the framing rectangle of this
         * <code>Shape</code> to the specified rectangular values.
         *
         * @param x
         * the X coordinate of the upper-left corner of the specified
         * rectangular shape
         * @param y
         * the Y coordinate of the upper-left corner of the specified
         * rectangular shape
         * @param w
         * the width of the specified rectangular shape
         * @param h
         * the height of the specified rectangular shape
         * @see #getFrame
         * @since 1.2
         */
        setFrame$double$double$double$double(x: number, y: number, w: number, h: number): void;
        /**
         * Sets the location and size of the framing rectangle of this
         * <code>Shape</code> to the specified {@link Point2D} and
         * {@link Dimension2D}, respectively. The framing rectangle is used by the
         * subclasses of <code>RectangularShape</code> to define their geometry.
         *
         * @param loc
         * the specified <code>Point2D</code>
         * @param size
         * the specified <code>Dimension2D</code>
         * @see #getFrame
         * @since 1.2
         */
        setFrame$java_awt_geom_Point2D$java_awt_geom_Dimension2D(loc: java.awt.geom.Point2D, size: java.awt.geom.Dimension2D): void;
        /**
         * Sets the framing rectangle of this <code>Shape</code> to be the specified
         * <code>Rectangle2D</code>. The framing rectangle is used by the subclasses
         * of <code>RectangularShape</code> to define their geometry.
         *
         * @param r
         * the specified <code>Rectangle2D</code>
         * @see #getFrame
         * @since 1.2
         */
        setFrame$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): void;
        /**
         * Sets the diagonal of the framing rectangle of this <code>Shape</code>
         * based on the two specified coordinates. The framing rectangle is used by
         * the subclasses of <code>RectangularShape</code> to define their geometry.
         *
         * @param x1
         * the X coordinate of the start point of the specified diagonal
         * @param y1
         * the Y coordinate of the start point of the specified diagonal
         * @param x2
         * the X coordinate of the end point of the specified diagonal
         * @param y2
         * the Y coordinate of the end point of the specified diagonal
         * @since 1.2
         */
        setFrameFromDiagonal(x1?: any, y1?: any, x2?: any, y2?: any): any;
        /**
         * Sets the diagonal of the framing rectangle of this <code>Shape</code>
         * based on two specified <code>Point2D</code> objects. The framing
         * rectangle is used by the subclasses of <code>RectangularShape</code> to
         * define their geometry.
         *
         * @param p1
         * the start <code>Point2D</code> of the specified diagonal
         * @param p2
         * the end <code>Point2D</code> of the specified diagonal
         * @since 1.2
         */
        setFrameFromDiagonal$java_awt_geom_Point2D$java_awt_geom_Point2D(p1: java.awt.geom.Point2D, p2: java.awt.geom.Point2D): void;
        /**
         * Sets the framing rectangle of this <code>Shape</code> based on the
         * specified center point coordinates and corner point coordinates. The
         * framing rectangle is used by the subclasses of
         * <code>RectangularShape</code> to define their geometry.
         *
         * @param centerX
         * the X coordinate of the specified center point
         * @param centerY
         * the Y coordinate of the specified center point
         * @param cornerX
         * the X coordinate of the specified corner point
         * @param cornerY
         * the Y coordinate of the specified corner point
         * @since 1.2
         */
        setFrameFromCenter(centerX?: any, centerY?: any, cornerX?: any, cornerY?: any): any;
        /**
         * Sets the framing rectangle of this <code>Shape</code> based on a
         * specified center <code>Point2D</code> and corner <code>Point2D</code>.
         * The framing rectangle is used by the subclasses of
         * <code>RectangularShape</code> to define their geometry.
         *
         * @param center
         * the specified center <code>Point2D</code>
         * @param corner
         * the specified corner <code>Point2D</code>
         * @since 1.2
         */
        setFrameFromCenter$java_awt_geom_Point2D$java_awt_geom_Point2D(center: java.awt.geom.Point2D, corner: java.awt.geom.Point2D): void;
        contains(x?: any, y?: any, w?: any, h?: any, origrect?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$java_awt_geom_Point2D(p: java.awt.geom.Point2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        getBounds(): java.awt.Rectangle;
        /**
         * Returns an iterator object that iterates along the <code>Shape</code>
         * object's boundary and provides access to a flattened view of the outline
         * of the <code>Shape</code> object's geometry.
         * <p>
         * Only SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point types will be returned
         * by the iterator.
         * <p>
         * The amount of subdivision of the curved segments is controlled by the
         * <code>flatness</code> parameter, which specifies the maximum distance
         * that any point on the unflattened transformed curve can deviate from the
         * returned flattened path segments. An optional {@link AffineTransform} can
         * be specified so that the coordinates returned in the iteration are
         * transformed accordingly.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired.
         * @param flatness
         * the maximum distance that the line segments used to
         * approximate the curved segments are allowed to deviate from
         * any point on the original curve
         * @return a <code>PathIterator</code> object that provides access to the
         * <code>Shape</code> object's flattened geometry.
         * @since 1.2
         */
        getPathIterator(at?: any, flatness?: any): any;
        /**
         * Creates a new object of the same class and with the same contents as this
         * object.
         *
         * @return a clone of this instance.
         * @exception OutOfMemoryError
         * if there is not enough memory.
         * @see java.lang.Cloneable
         * @since 1.2
         */
        clone(): any;
    }
}
declare namespace java.awt.geom {
    /**
     * A utility class to iterate over the path segments of a rectangle through the
     * PathIterator interface.
     *
     * @author Jim Graham
     */
    class RectIterator implements java.awt.geom.PathIterator {
        x: number;
        y: number;
        w: number;
        h: number;
        affine: java.awt.geom.AffineTransform;
        index: number;
        constructor(r: java.awt.geom.Rectangle2D, at: java.awt.geom.AffineTransform);
        /**
         * Return the winding rule for determining the insideness of the path.
         *
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if there are more points to read.
         *
         * @return true if there are more points to read
         */
        isDone(): boolean;
        next(doNext?: any): any;
        /**
         * Moves the iterator to the next segment of the path forwards along the
         * primary direction of traversal as long as there are more points in that
         * direction.
         */
        next$(): void;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of float x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment(coords?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of double x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.awt.geom {
    /**
     * A utility class to iterate over the path segments of an rounded rectangle
     * through the PathIterator interface.
     *
     * @author Jim Graham
     */
    class RoundRectIterator implements java.awt.geom.PathIterator {
        x: number;
        y: number;
        w: number;
        h: number;
        aw: number;
        ah: number;
        affine: java.awt.geom.AffineTransform;
        index: number;
        constructor(rr: java.awt.geom.RoundRectangle2D, at: java.awt.geom.AffineTransform);
        /**
         * Return the winding rule for determining the insideness of the path.
         *
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         */
        getWindingRule(): number;
        /**
         * Tests if there are more points to read.
         *
         * @return true if there are more points to read
         */
        isDone(): boolean;
        next(doNext?: any): any;
        /**
         * Moves the iterator to the next segment of the path forwards along the
         * primary direction of traversal as long as there are more points in that
         * direction.
         */
        next$(): void;
        static angle: number;
        static angle_$LI$(): number;
        static a: number;
        static a_$LI$(): number;
        static b: number;
        static b_$LI$(): number;
        static c: number;
        static c_$LI$(): number;
        static cv: number;
        static cv_$LI$(): number;
        static acv: number;
        static acv_$LI$(): number;
        static ctrlpts: number[][];
        static ctrlpts_$LI$(): number[][];
        static types: number[];
        static types_$LI$(): number[];
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of float x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment(coords?: any): any;
        /**
         * Returns the coordinates and type of the current path segment in the
         * iteration. The return value is the path segment type: SEG_MOVETO,
         * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of
         * length 6 must be passed in and may be used to store the coordinates of
         * the point(s). Each point is stored as a pair of double x,y coordinates.
         * SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will
         * return two points, SEG_CUBICTO will return 3 points and SEG_CLOSE will
         * not return any points.
         *
         * @see #SEG_MOVETO
         * @see #SEG_LINETO
         * @see #SEG_QUADTO
         * @see #SEG_CUBICTO
         * @see #SEG_CLOSE
         */
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.awt {
    abstract class Graphics {
        abstract drawString(s: string, x: number, y: number): any;
    }
}
declare namespace java.awt {
    class GridLayout implements java.awt.Layout {
        table: HTMLTableElement;
        currentPosition: number;
        cols: number;
        rows: number;
        constructor(rows: number, cols: number);
        add(component: java.awt.HTMLComponent): void;
        getHTMLElement(): HTMLElement;
        bind(id: string): void;
        init(): void;
    }
}
declare namespace java.awt {
    interface HTMLComponent {
        getHTMLElement(): HTMLElement;
        bind(id: string): any;
        init(): any;
    }
}
declare namespace java.awt {
    interface Layout extends java.awt.HTMLComponent {
        add(component: java.awt.HTMLComponent): any;
    }
}
declare namespace java.awt {
    /**
     * The <code>Shape</code> interface provides definitions for objects
     * that represent some form of geometric shape.  The <code>Shape</code>
     * is described by a {@link PathIterator} object, which can express the
     * outline of the <code>Shape</code> as well as a rule for determining
     * how the outline divides the 2D plane into interior and exterior
     * points.  Each <code>Shape</code> object provides callbacks to get the
     * bounding box of the geometry, determine whether points or
     * rectangles lie partly or entirely within the interior
     * of the <code>Shape</code>, and retrieve a <code>PathIterator</code>
     * object that describes the trajectory path of the <code>Shape</code>
     * outline.
     * <p>
     * <a name="def_insideness"><b>Definition of insideness:</b></a>
     * A point is considered to lie inside a
     * <code>Shape</code> if and only if:
     * <ul>
     * <li> it lies completely
     * inside the<code>Shape</code> boundary <i>or</i>
     * <li>
     * it lies exactly on the <code>Shape</code> boundary <i>and</i> the
     * space immediately adjacent to the
     * point in the increasing <code>X</code> direction is
     * entirely inside the boundary <i>or</i>
     * <li>
     * it lies exactly on a horizontal boundary segment <b>and</b> the
     * space immediately adjacent to the point in the
     * increasing <code>Y</code> direction is inside the boundary.
     * </ul>
     * <p>The <code>contains</code> and <code>intersects</code> methods
     * consider the interior of a <code>Shape</code> to be the area it
     * encloses as if it were filled.  This means that these methods
     * consider
     * unclosed shapes to be implicitly closed for the purpose of
     * determining if a shape contains or intersects a rectangle or if a
     * shape contains a point.
     *
     * @see java.awt.geom.PathIterator
     * @see java.awt.geom.AffineTransform
     * @see java.awt.geom.FlatteningPathIterator
     * @see java.awt.geom.GeneralPath
     *
     * @author Jim Graham
     * @since 1.2
     */
    interface Shape {
        /**
         * Returns an integer {@link Rectangle} that completely encloses the
         * <code>Shape</code>.  Note that there is no guarantee that the
         * returned <code>Rectangle</code> is the smallest bounding box that
         * encloses the <code>Shape</code>, only that the <code>Shape</code>
         * lies entirely within the indicated  <code>Rectangle</code>.  The
         * returned <code>Rectangle</code> might also fail to completely
         * enclose the <code>Shape</code> if the <code>Shape</code> overflows
         * the limited range of the integer data type.  The
         * <code>getBounds2D</code> method generally returns a
         * tighter bounding box due to its greater flexibility in
         * representation.
         *
         * <p>
         * Note that the <a href="{@docRoot}/java/awt/Shape.html#def_insideness">
         * definition of insideness</a> can lead to situations where points
         * on the defining outline of the {@code shape} may not be considered
         * contained in the returned {@code bounds} object, but only in cases
         * where those points are also not considered contained in the original
         * {@code shape}.
         * </p>
         * <p>
         * If a {@code point} is inside the {@code shape} according to the
         * {@link #contains(double x, double y) contains(point)} method, then
         * it must be inside the returned {@code Rectangle} bounds object
         * according to the {@link #contains(double x, double y) contains(point)}
         * method of the {@code bounds}. Specifically:
         * </p>
         * <p>
         * {@code shape.contains(x,y)} requires {@code bounds.contains(x,y)}
         * </p>
         * <p>
         * If a {@code point} is not inside the {@code shape}, then it might
         * still be contained in the {@code bounds} object:
         * </p>
         * <p>
         * {@code bounds.contains(x,y)} does not imply {@code shape.contains(x,y)}
         * </p>
         * @return an integer <code>Rectangle</code> that completely encloses
         * the <code>Shape</code>.
         * @see #getBounds2D
         * @since 1.2
         */
        getBounds(): java.awt.Rectangle;
        /**
         * Returns a high precision and more accurate bounding box of
         * the <code>Shape</code> than the <code>getBounds</code> method.
         * Note that there is no guarantee that the returned
         * {@link Rectangle2D} is the smallest bounding box that encloses
         * the <code>Shape</code>, only that the <code>Shape</code> lies
         * entirely within the indicated <code>Rectangle2D</code>.  The
         * bounding box returned by this method is usually tighter than that
         * returned by the <code>getBounds</code> method and never fails due
         * to overflow problems since the return value can be an instance of
         * the <code>Rectangle2D</code> that uses double precision values to
         * store the dimensions.
         *
         * <p>
         * Note that the <a href="{@docRoot}/java/awt/Shape.html#def_insideness">
         * definition of insideness</a> can lead to situations where points
         * on the defining outline of the {@code shape} may not be considered
         * contained in the returned {@code bounds} object, but only in cases
         * where those points are also not considered contained in the original
         * {@code shape}.
         * </p>
         * <p>
         * If a {@code point} is inside the {@code shape} according to the
         * {@link #contains(Point2D p) contains(point)} method, then it must
         * be inside the returned {@code Rectangle2D} bounds object according
         * to the {@link #contains(Point2D p) contains(point)} method of the
         * {@code bounds}. Specifically:
         * </p>
         * <p>
         * {@code shape.contains(p)} requires {@code bounds.contains(p)}
         * </p>
         * <p>
         * If a {@code point} is not inside the {@code shape}, then it might
         * still be contained in the {@code bounds} object:
         * </p>
         * <p>
         * {@code bounds.contains(p)} does not imply {@code shape.contains(p)}
         * </p>
         * @return an instance of <code>Rectangle2D</code> that is a
         * high-precision bounding box of the <code>Shape</code>.
         * @see #getBounds
         * @since 1.2
         */
        getBounds2D(): java.awt.geom.Rectangle2D;
        contains(x?: any, y?: any, w?: any, h?: any, origrect?: any): any;
        /**
         * Tests if the interior of the <code>Shape</code> intersects the
         * interior of a specified rectangular area.
         * The rectangular area is considered to intersect the <code>Shape</code>
         * if any point is contained in both the interior of the
         * <code>Shape</code> and the specified rectangular area.
         * <p>
         * The {@code Shape.intersects()} method allows a {@code Shape}
         * implementation to conservatively return {@code true} when:
         * <ul>
         * <li>
         * there is a high probability that the rectangular area and the
         * <code>Shape</code> intersect, but
         * <li>
         * the calculations to accurately determine this intersection
         * are prohibitively expensive.
         * </ul>
         * This means that for some {@code Shapes} this method might
         * return {@code true} even though the rectangular area does not
         * intersect the {@code Shape}.
         * The {@link java.awt.geom.Area Area} class performs
         * more accurate computations of geometric intersection than most
         * {@code Shape} objects and therefore can be used if a more precise
         * answer is required.
         *
         * @param x the X coordinate of the upper-left corner
         * of the specified rectangular area
         * @param y the Y coordinate of the upper-left corner
         * of the specified rectangular area
         * @param w the width of the specified rectangular area
         * @param h the height of the specified rectangular area
         * @return <code>true</code> if the interior of the <code>Shape</code> and
         * the interior of the rectangular area intersect, or are
         * both highly likely to intersect and intersection calculations
         * would be too expensive to perform; <code>false</code> otherwise.
         * @see java.awt.geom.Area
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Returns an iterator object that iterates along the <code>Shape</code>
         * boundary and provides access to a flattened view of the
         * <code>Shape</code> outline geometry.
         * <p>
         * Only SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point types are
         * returned by the iterator.
         * <p>
         * If an optional <code>AffineTransform</code> is specified,
         * the coordinates returned in the iteration are transformed
         * accordingly.
         * <p>
         * The amount of subdivision of the curved segments is controlled
         * by the <code>flatness</code> parameter, which specifies the
         * maximum distance that any point on the unflattened transformed
         * curve can deviate from the returned flattened path segments.
         * Note that a limit on the accuracy of the flattened path might be
         * silently imposed, causing very small flattening parameters to be
         * treated as larger values.  This limit, if there is one, is
         * defined by the particular implementation that is used.
         * <p>
         * Each call to this method returns a fresh <code>PathIterator</code>
         * object that traverses the <code>Shape</code> object geometry
         * independently from any other <code>PathIterator</code> objects in use at
         * the same time.
         * <p>
         * It is recommended, but not guaranteed, that objects
         * implementing the <code>Shape</code> interface isolate iterations
         * that are in process from any changes that might occur to the original
         * object's geometry during such iterations.
         *
         * @param at an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @param flatness the maximum distance that the line segments used to
         * approximate the curved segments are allowed to deviate
         * from any point on the original curve
         * @return a new <code>PathIterator</code> that independently traverses
         * a flattened view of the geometry of the  <code>Shape</code>.
         * @since 1.2
         */
        getPathIterator(at?: any, flatness?: any): any;
    }
}
declare namespace java.beans {
    /**
     * General-purpose beans control methods. GWT only supports a limited subset of these methods. Only
     * the documented methods are available.
     */
    class Beans {
        /**
         * @return <code>true</code> if we are running in the design time mode.
         */
        static isDesignTime(): boolean;
    }
}
declare namespace java.io {
    /**
     * An {@code AutoCloseable} whose close method may throw an {@link IOException}.
     */
    interface Closeable extends java.lang.AutoCloseable {
        /**
         * Closes the object and release any system resources it holds.
         *
         * <p>Although only the first call has any effect, it is safe to call close
         * multiple times on the same object. This is more lenient than the
         * overridden {@code AutoCloseable.close()}, which may be called at most
         * once.
         */
        close(): any;
    }
}
declare namespace java.io {
    interface FileFilter {
        (pathname: java.io.File): boolean;
    }
}
declare namespace java.io {
    /**
     * Package-private abstract class for the local filesystem abstraction.
     */
    abstract class FileSystem {
        static __static_initialized: boolean;
        static __static_initialize(): void;
        /**
         * Return the local filesystem's name-separator character.
         */
        abstract getSeparator(): string;
        /**
         * Return the local filesystem's path-separator character.
         */
        abstract getPathSeparator(): string;
        normalize(pathname?: any, len?: any, off?: any): any;
        /**
         * Convert the given pathname string to normal form.  If the string is
         * already in normal form then it is simply returned.
         */
        normalize$java_lang_String(path: string): string;
        /**
         * Compute the length of this pathname string's prefix.  The pathname
         * string must be in normal form.
         */
        abstract prefixLength(path: string): number;
        /**
         * Resolve the child pathname string against the parent.
         * Both strings must be in normal form, and the result
         * will be in normal form.
         */
        resolve(parent?: any, child?: any): any;
        /**
         * Return the parent pathname string to be used when the parent-directory
         * argument in one of the two-argument File constructors is the empty
         * pathname.
         */
        abstract getDefaultParent(): string;
        /**
         * Post-process the given URI path string if necessary.  This is used on
         * win32, e.g., to transform "/c:/foo" into "c:/foo".  The path string
         * still has slash separators; code in the File class will translate them
         * after this method returns.
         */
        abstract fromURIPath(path: string): string;
        /**
         * Tell whether or not the given abstract pathname is absolute.
         */
        abstract isAbsolute(f: java.io.File): boolean;
        /**
         * Resolve the given abstract pathname into absolute form.  Invoked by the
         * getAbsolutePath and getCanonicalPath methods in the File class.
         */
        resolve$java_io_File(f: java.io.File): string;
        abstract canonicalize(path: string): string;
        static BA_EXISTS: number;
        static BA_REGULAR: number;
        static BA_DIRECTORY: number;
        static BA_HIDDEN: number;
        /**
         * Return the simple boolean attributes for the file or directory denoted
         * by the given abstract pathname, or zero if it does not exist or some
         * other I/O error occurs.
         */
        abstract getBooleanAttributes(f: java.io.File): number;
        static ACCESS_READ: number;
        static ACCESS_WRITE: number;
        static ACCESS_EXECUTE: number;
        /**
         * Check whether the file or directory denoted by the given abstract
         * pathname may be accessed by this process.  The second argument specifies
         * which access, ACCESS_READ, ACCESS_WRITE or ACCESS_EXECUTE, to check.
         * Return false if access is denied or an I/O error occurs
         */
        abstract checkAccess(f: java.io.File, access: number): boolean;
        /**
         * Set on or off the access permission (to owner only or to all) to the file
         * or directory denoted by the given abstract pathname, based on the parameters
         * enable, access and oweronly.
         */
        abstract setPermission(f: java.io.File, access: number, enable: boolean, owneronly: boolean): boolean;
        /**
         * Return the time at which the file or directory denoted by the given
         * abstract pathname was last modified, or zero if it does not exist or
         * some other I/O error occurs.
         */
        abstract getLastModifiedTime(f: java.io.File): number;
        /**
         * Return the length in bytes of the file denoted by the given abstract
         * pathname, or zero if it does not exist, is a directory, or some other
         * I/O error occurs.
         */
        abstract getLength(f: java.io.File): number;
        /**
         * Create a new empty file with the given pathname.  Return
         * <code>true</code> if the file was created and <code>false</code> if a
         * file or directory with the given pathname already exists.  Throw an
         * IOException if an I/O error occurs.
         */
        abstract createFileExclusively(pathname: string): boolean;
        /**
         * Delete the file or directory denoted by the given abstract pathname,
         * returning <code>true</code> if and only if the operation succeeds.
         */
        abstract delete(f: java.io.File): boolean;
        /**
         * List the elements of the directory denoted by the given abstract
         * pathname.  Return an array of strings naming the elements of the
         * directory if successful; otherwise, return <code>null</code>.
         */
        abstract list(f: java.io.File): string[];
        /**
         * Create a new directory denoted by the given abstract pathname,
         * returning <code>true</code> if and only if the operation succeeds.
         */
        abstract createDirectory(f: java.io.File): boolean;
        /**
         * Rename the file or directory denoted by the first abstract pathname to
         * the second abstract pathname, returning <code>true</code> if and only if
         * the operation succeeds.
         */
        abstract rename(f1: java.io.File, f2: java.io.File): boolean;
        /**
         * Set the last-modified time of the file or directory denoted by the
         * given abstract pathname, returning <code>true</code> if and only if the
         * operation succeeds.
         */
        abstract setLastModifiedTime(f: java.io.File, time: number): boolean;
        /**
         * Mark the file or directory denoted by the given abstract pathname as
         * read-only, returning <code>true</code> if and only if the operation
         * succeeds.
         */
        abstract setReadOnly(f: java.io.File): boolean;
        /**
         * List the available filesystem roots.
         */
        abstract listRoots(): java.io.File[];
        static SPACE_TOTAL: number;
        static SPACE_FREE: number;
        static SPACE_USABLE: number;
        abstract getSpace(f: java.io.File, t: number): number;
        /**
         * Compare two abstract pathnames lexicographically.
         */
        abstract compare(f1: java.io.File, f2: java.io.File): number;
        /**
         * Compute the hash code of an abstract pathname.
         */
        abstract hashCode(f: java.io.File): number;
        static useCanonCaches: boolean;
        static useCanonPrefixCache: boolean;
        private static getBooleanProperty(prop, defaultVal);
        static __static_initializer_0(): void;
    }
}
declare namespace java.io {
    /**
     * Defines an interface for classes that can (or need to) be flushed, typically
     * before some output processing is considered to be finished and the object
     * gets closed.
     */
    interface Flushable {
        /**
         * Flushes the object by writing out any buffered data to the underlying
         * output.
         *
         * @throws IOException
         * if there are any issues writing the data.
         */
        flush(): any;
    }
}
declare namespace java.io {
    /**
     * A readable source of bytes.
     *
     * <p>Most clients will use input streams that read data from the file system
     * ({@link FileInputStream}), the network ({@link java.net.Socket#getInputStream()}/{@link
     * java.net.HttpURLConnection#getInputStream()}), or from an in-memory byte
     * array ({@link ByteArrayInputStream}).
     *
     * <p>Use {@link InputStreamReader} to adapt a byte stream like this one into a
     * character stream.
     *
     * <p>Most clients should wrap their input stream with {@link
     * BufferedInputStream}. Callers that do only bulk reads may omit buffering.
     *
     * <p>Some implementations support marking a position in the input stream and
     * resetting back to this position later. Implementations that don't return
     * false from {@link #markSupported()} and throw an {@link IOException} when
     * {@link #reset()} is called.
     *
     * <h3>Subclassing InputStream</h3>
     * Subclasses that decorate another input stream should consider subclassing
     * {@link FilterInputStream}, which delegates all calls to the source input
     * stream.
     *
     * <p>All input stream subclasses should override <strong>both</strong> {@link
     * #read() read()} and {@link #read(byte[],int,int) read(byte[],int,int)}. The
     * three argument overload is necessary for bulk access to the data. This is
     * much more efficient than byte-by-byte access.
     *
     * @see OutputStream
     */
    abstract class InputStream implements java.io.Closeable {
        /**
         * Size of the temporary buffer used when skipping bytes with {@link skip(long)}.
         */
        static MAX_SKIP_BUFFER_SIZE: number;
        /**
         * This constructor does nothing. It is provided for signature
         * compatibility.
         */
        constructor();
        /**
         * Returns an estimated number of bytes that can be read or skipped without blocking for more
         * input.
         *
         * <p>Note that this method provides such a weak guarantee that it is not very useful in
         * practice.
         *
         * <p>Firstly, the guarantee is "without blocking for more input" rather than "without
         * blocking": a read may still block waiting for I/O to complete&nbsp;&mdash; the guarantee is
         * merely that it won't have to wait indefinitely for data to be written. The result of this
         * method should not be used as a license to do I/O on a thread that shouldn't be blocked.
         *
         * <p>Secondly, the result is a
         * conservative estimate and may be significantly smaller than the actual number of bytes
         * available. In particular, an implementation that always returns 0 would be correct.
         * In general, callers should only use this method if they'd be satisfied with
         * treating the result as a boolean yes or no answer to the question "is there definitely
         * data ready?".
         *
         * <p>Thirdly, the fact that a given number of bytes is "available" does not guarantee that a
         * read or skip will actually read or skip that many bytes: they may read or skip fewer.
         *
         * <p>It is particularly important to realize that you <i>must not</i> use this method to
         * size a container and assume that you can read the entirety of the stream without needing
         * to resize the container. Such callers should probably write everything they read to a
         * {@link ByteArrayOutputStream} and convert that to a byte array. Alternatively, if you're
         * reading from a file, {@link File#length} returns the current length of the file (though
         * assuming the file's length can't change may be incorrect, reading a file is inherently
         * racy).
         *
         * <p>The default implementation of this method in {@code InputStream} always returns 0.
         * Subclasses should override this method if they are able to indicate the number of bytes
         * available.
         *
         * @return the estimated number of bytes available
         * @throws IOException if this stream is closed or an error occurs
         */
        available(): number;
        /**
         * Closes this stream. Concrete implementations of this class should free
         * any resources during close. This implementation does nothing.
         *
         * @throws IOException
         * if an error occurs while closing this stream.
         */
        close(): void;
        /**
         * Sets a mark position in this InputStream. The parameter {@code readlimit}
         * indicates how many bytes can be read before the mark is invalidated.
         * Sending {@code reset()} will reposition the stream back to the marked
         * position provided {@code readLimit} has not been surpassed.
         * <p>
         * This default implementation does nothing and concrete subclasses must
         * provide their own implementation.
         *
         * @param readlimit
         * the number of bytes that can be read from this stream before
         * the mark is invalidated.
         * @see #markSupported()
         * @see #reset()
         */
        mark(readlimit: number): void;
        /**
         * Indicates whether this stream supports the {@code mark()} and
         * {@code reset()} methods. The default implementation returns {@code false}.
         *
         * @return always {@code false}.
         * @see #mark(int)
         * @see #reset()
         */
        markSupported(): boolean;
        /**
         * Reads a single byte from this stream and returns it as an integer in the
         * range from 0 to 255. Returns -1 if the end of the stream has been
         * reached. Blocks until one byte has been read, the end of the source
         * stream is detected or an exception is thrown.
         *
         * @throws IOException
         * if the stream is closed or another IOException occurs.
         */
        read$(): number;
        /**
         * Equivalent to {@code read(buffer, 0, buffer.length)}.
         */
        read$byte_A(buffer: number[]): number;
        /**
         * Reads up to {@code byteCount} bytes from this stream and stores them in
         * the byte array {@code buffer} starting at {@code byteOffset}.
         * Returns the number of bytes actually read or -1 if the end of the stream
         * has been reached.
         *
         * @throws IndexOutOfBoundsException
         * if {@code byteOffset < 0 || byteCount < 0 || byteOffset + byteCount > buffer.length}.
         * @throws IOException
         * if the stream is closed or another IOException occurs.
         */
        read(buffer?: any, byteOffset?: any, byteCount?: any): any;
        /**
         * Resets this stream to the last marked location. Throws an
         * {@code IOException} if the number of bytes read since the mark has been
         * set is greater than the limit provided to {@code mark}, or if no mark
         * has been set.
         * <p>
         * This implementation always throws an {@code IOException} and concrete
         * subclasses should provide the proper implementation.
         *
         * @throws IOException
         * if this stream is closed or another IOException occurs.
         */
        reset(): void;
        /**
         * Skips at most {@code byteCount} bytes in this stream. The number of actual
         * bytes skipped may be anywhere between 0 and {@code byteCount}. If
         * {@code byteCount} is negative, this method does nothing and returns 0, but
         * some subclasses may throw.
         *
         * <p>Note the "at most" in the description of this method: this method may
         * choose to skip fewer bytes than requested. Callers should <i>always</i>
         * check the return value.
         *
         * <p>This default implementation reads bytes into a temporary buffer. Concrete
         * subclasses should provide their own implementation.
         *
         * @return the number of bytes actually skipped.
         * @throws IOException if this stream is closed or another IOException
         * occurs.
         */
        skip(byteCount: number): number;
    }
}
declare namespace java.io {
    /**
     * Provides a series of utilities to be reused between IO classes.
     *
     * TODO(chehayeb): move these checks to InternalPreconditions.
     */
    class IOUtils {
        /**
         * Validates the offset and the byte count for the given array of bytes.
         *
         * @param buffer Array of bytes to be checked.
         * @param byteOffset Starting offset in the array.
         * @param byteCount Total number of bytes to be accessed.
         * @throws NullPointerException if the given reference to the buffer is null.
         * @throws IndexOutOfBoundsException if {@code byteOffset} is negative, {@code byteCount} is
         * negative or their sum exceeds the buffer length.
         */
        static checkOffsetAndCount(buffer?: any, byteOffset?: any, byteCount?: any): any;
        /**
         * Validates the offset and the byte count for the given array of characters.
         *
         * @param buffer Array of characters to be checked.
         * @param charOffset Starting offset in the array.
         * @param charCount Total number of characters to be accessed.
         * @throws NullPointerException if the given reference to the buffer is null.
         * @throws IndexOutOfBoundsException if {@code charOffset} is negative, {@code charCount} is
         * negative or their sum exceeds the buffer length.
         */
        static checkOffsetAndCount$char_A$int$int(buffer: string[], charOffset: number, charCount: number): void;
        /**
         * Validates the offset and the byte count for the given array length.
         *
         * @param length Length of the array to be checked.
         * @param offset Starting offset in the array.
         * @param count Total number of elements to be accessed.
         * @throws IndexOutOfBoundsException if {@code offset} is negative, {@code count} is negative or
         * their sum exceeds the given {@code length}.
         */
        private static checkOffsetAndCount$int$int$int(length, offset, count);
        constructor();
    }
}
declare namespace java.io {
    /**
     * A writable sink for bytes.
     *
     * <p>Most clients will use output streams that write data to the file system
     * ({@link FileOutputStream}), the network ({@link java.net.Socket#getOutputStream()}/{@link
     * java.net.HttpURLConnection#getOutputStream()}), or to an in-memory byte array
     * ({@link ByteArrayOutputStream}).
     *
     * <p>Use {@link OutputStreamWriter} to adapt a byte stream like this one into a
     * character stream.
     *
     * <p>Most clients should wrap their output stream with {@link
     * BufferedOutputStream}. Callers that do only bulk writes may omit buffering.
     *
     * <h3>Subclassing OutputStream</h3>
     * Subclasses that decorate another output stream should consider subclassing
     * {@link FilterOutputStream}, which delegates all calls to the target output
     * stream.
     *
     * <p>All output stream subclasses should override <strong>both</strong> {@link
     * #write(int)} and {@link #write(byte[],int,int) write(byte[],int,int)}. The
     * three argument overload is necessary for bulk access to the data. This is
     * much more efficient than byte-by-byte access.
     *
     * @see InputStream
     *
     * <p>The implementation provided by this class behaves as described in the Java
     * API documentation except for {@link write(int)} which throws an exception of
     * type {@link java.lang.UnsupportedOperationException} instead of being
     * abstract.
     */
    abstract class OutputStream implements java.io.Closeable, java.io.Flushable {
        /**
         * Default constructor.
         */
        constructor();
        /**
         * Closes this stream. Implementations of this method should free any
         * resources used by the stream. This implementation does nothing.
         *
         * @throws IOException
         * if an error occurs while closing this stream.
         */
        close(): void;
        /**
         * Flushes this stream. Implementations of this method should ensure that
         * any buffered data is written out. This implementation does nothing.
         *
         * @throws IOException
         * if an error occurs while flushing this stream.
         */
        flush(): void;
        /**
         * Equivalent to {@code write(buffer, 0, buffer.length)}.
         */
        write$byte_A(buffer: number[]): void;
        /**
         * Writes {@code count} bytes from the byte array {@code buffer} starting at
         * position {@code offset} to this stream.
         *
         * @param buffer
         * the buffer to be written.
         * @param offset
         * the start position in {@code buffer} from where to get bytes.
         * @param count
         * the number of bytes from {@code buffer} to write to this
         * stream.
         * @throws IOException
         * if an error occurs while writing to this stream.
         * @throws IndexOutOfBoundsException
         * if {@code offset < 0} or {@code count < 0}, or if
         * {@code offset + count} is bigger than the length of
         * {@code buffer}.
         */
        write(buffer?: any, offset?: any, count?: any): any;
        /**
         * Writes a single byte to this stream. Only the least significant byte of
         * the integer {@code oneByte} is written to the stream.
         *
         * @param oneByte
         * the byte to be written.
         * @throws IOException
         * if an error occurs while writing to this stream.
         */
        write$int(oneByte: number): void;
    }
}
declare namespace java.io {
    /**
     * JSweet implementation.
     */
    abstract class Reader implements java.io.Closeable {
        lock: any;
        constructor(lock?: any);
        read$(): number;
        read$char_A(cbuf: string[]): number;
        read(cbuf?: any, off?: any, len?: any): any;
        /**
         * Maximum skip-buffer size
         */
        static maxSkipBufferSize: number;
        /**
         * Skip buffer, null until allocated
         */
        private skipBuffer;
        skip(n: number): number;
        ready(): boolean;
        markSupported(): boolean;
        mark(readAheadLimit: number): void;
        reset(): void;
        abstract close(): any;
    }
}
declare namespace java.io {
    /**
     * Provided for interoperability; RPC treats this interface synonymously with
     * {@link com.google.gwt.user.client.rpc.IsSerializable IsSerializable}.
     * The Java serialization protocol is explicitly not supported.
     */
    interface Serializable {
    }
}
declare namespace java.io {
    /**
     * JSweet implementation.
     */
    abstract class Writer implements java.lang.Appendable, java.io.Closeable, java.io.Flushable {
        private writeBuffer;
        static WRITE_BUFFER_SIZE: number;
        lock: any;
        constructor(lock?: any);
        write$int(c: number): void;
        write$char_A(cbuf: string[]): void;
        write$char_A$int$int(cbuf: string[], off: number, len: number): void;
        write$java_lang_String(str: string): void;
        write(str?: any, off?: any, len?: any): any;
        append$java_lang_CharSequence(csq: string): Writer;
        append(csq?: any, start?: any, end?: any): any;
        append$char(c: string): Writer;
        abstract flush(): any;
        abstract close(): any;
    }
}
declare namespace java.lang {
    /**
     * A base class to share implementation between {@link StringBuffer} and {@link StringBuilder}.
     * <p>
     * Most methods will give expected performance results. Exception is {@link #setCharAt(int, char)},
     * which is O(n), and thus should not be used many times on the same <code>StringBuffer</code>.
     */
    abstract class AbstractStringBuilder {
        string: string;
        constructor(string: string);
        length(): number;
        setLength(newLength: number): void;
        capacity(): number;
        ensureCapacity(ignoredCapacity: number): void;
        trimToSize(): void;
        charAt(index: number): string;
        getChars(srcStart: number, srcEnd: number, dst: string[], dstStart: number): void;
        /**
         * Warning! This method is <b>much</b> slower than the JRE implementation. If you need to do
         * character level manipulation, you are strongly advised to use a char[] directly.
         */
        setCharAt(index: number, x: string): void;
        subSequence(start: number, end: number): string;
        substring$int(begin: number): string;
        substring(begin?: any, end?: any): any;
        indexOf$java_lang_String(x: string): number;
        indexOf(x?: any, start?: any): any;
        lastIndexOf$java_lang_String(s: string): number;
        lastIndexOf(s?: any, start?: any): any;
        toString(): string;
        append0(x: string, start: number, end: number): void;
        appendCodePoint0(x: number): void;
        replace0(start: number, end: number, toInsert: string): void;
        reverse0(): void;
        private static swap(buffer, f, s);
    }
}
declare namespace java.lang.annotation {
    /**
     * Base interface for all annotation types <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/Annotation.html">[Sun
     * docs]</a>.
     */
    interface Annotation {
        annotationType(): any;
        equals(obj: any): boolean;
        hashCode(): number;
        toString(): string;
    }
}
declare namespace java.lang.annotation {
    /**
     * Indicates the annotation parser determined the annotation was malformed when
     * reading from the class file <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/AnnotationFormatError.html">[Sun
     * docs]</a>.
     */
    class AnnotationFormatError extends Error {
        constructor();
    }
}
declare namespace java.lang.annotation {
    /**
     * Annotation which indicates annotations should be documented by javadoc/etc <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/Documented.html">[Sun
     * docs]</a>.
     */
    interface Documented {
    }
}
declare namespace java.lang.annotation {
    /**
     * Enumerates types of declared elements in a Java program <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/ElementType.html">[Sun
     * docs]</a>.
     */
    enum ElementType {
        ANNOTATION_TYPE = 0,
        CONSTRUCTOR = 1,
        FIELD = 2,
        LOCAL_VARIABLE = 3,
        METHOD = 4,
        PACKAGE = 5,
        PARAMETER = 6,
        TYPE = 7,
    }
}
declare namespace java.lang.annotation {
    /**
     * Annotation which indicates an annotation type is automatically inherited <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/Inherited.html">[Sun
     * docs]</a>.
     */
    interface Inherited {
    }
}
declare namespace java.lang.annotation {
    /**
     * Annotation which indicates how long annotations should be retained <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/Retention.html">[Sun
     * doc]</a>.
     */
    interface Retention {
        value(): java.lang.annotation.RetentionPolicy;
    }
}
declare namespace java.lang.annotation {
    /**
     * Enumerates annotation retention policies <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/RetentionPolicy.html">[Sun
     * docs]</a>.
     */
    enum RetentionPolicy {
        CLASS = 0,
        RUNTIME = 1,
        SOURCE = 2,
    }
}
declare namespace java.lang.annotation {
    /**
     * Annotation which indicates the kinds of program element to which an
     * annotation type is applicable <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/Target.html">[Sun
     * docs]</a>.
     */
    interface Target {
        value(): java.lang.annotation.ElementType[];
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/javase/6/docs/api/java/lang/Appendable.html">the
     * official Java API doc</a> for details.
     */
    interface Appendable {
        append(x?: any, start?: any, len?: any): any;
    }
}
declare namespace java.lang {
    /**
     * Represents an error caused by an assertion failure.
     */
    class AssertionError extends Error {
        constructor(message?: any, cause?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://docs.oracle.com/javase/7/docs/api/java/lang/AutoCloseable.html">the
     * official Java API doc</a> for details.
     */
    interface AutoCloseable {
        /**
         * Closes this resource.
         */
        close(): any;
    }
}
declare namespace java.lang {
    /**
     * Abstracts the notion of a sequence of characters.
     */
    interface CharSequence {
        charAt(index: number): string;
        length(): number;
        subSequence(start: number, end: number): string;
        toString(): string;
    }
}
declare namespace java.lang {
    /**
     * Generally unsupported. This class is provided so that the GWT compiler can
     * choke down class literal references.
     * <p>
     * NOTE: The code in this class is very sensitive and should keep its
     * dependencies upon other classes to a minimum.
     *
     * @param <T>
     * the type of the object
     */
    class Class<T> implements java.lang.reflect.Type {
        static constructors: Array<Function>;
        static constructors_$LI$(): Array<Function>;
        static classes: Array<any>;
        static classes_$LI$(): Array<any>;
        static getConstructorForClass(clazz: any): Function;
        static getClassForConstructor(constructor: Function): any;
        static mapConstructorToClass(constructor: Function, clazz: any): void;
        static PRIMITIVE: number;
        static INTERFACE: number;
        static ARRAY: number;
        static ENUM: number;
        /**
         * Create a Class object for an array.
         * <p>
         *
         * Arrays are not registered in the prototype table and get the class
         * literal explicitly at construction.
         * <p>
         */
        private static getClassLiteralForArray<T>(leafClass, dimensions);
        private createClassLiteralForArray(dimensions);
        /**
         * Create a Class object for a class.
         *
         * @skip
         */
        static createForClass<T>(packageName: string, compoundClassName: string, typeId: string, superclass: any): any;
        /**
         * Create a Class object for an enum.
         *
         * @skip
         */
        static createForEnum<T>(packageName: string, compoundClassName: string, typeId: string, superclass: any, enumConstantsFunc: Function, enumValueOfFunc: Function): any;
        /**
         * Create a Class object for an interface.
         *
         * @skip
         */
        static createForInterface<T>(packageName: string, compoundClassName: string): any;
        /**
         * Create a Class object for a primitive.
         *
         * @skip
         */
        static createForPrimitive(className: string, primitiveTypeId: string): any;
        /**
         * Used by {@link WebModePayloadSink} to create uninitialized instances.
         */
        static getPrototypeForClass(clazz: any): any;
        /**
         * Creates the class object for a type and initiliazes its fields.
         */
        private static createClassObject<T>(packageName, compoundClassName, typeId);
        /**
         * Initiliazes {@code clazz} names from metadata.
         * <p>
         * Written in JSNI to minimize dependencies (on String.+).
         */
        private static initializeNames(clazz);
        /**
         * Sets the class object for primitives.
         * <p>
         * Written in JSNI to minimize dependencies (on (String)+).
         */
        static synthesizePrimitiveNamesFromTypeId(clazz: any, primitiveTypeId: Object): void;
        enumValueOfFunc: Function;
        modifiers: number;
        private componentType;
        private enumConstantsFunc;
        private enumSuperclass;
        private superclass;
        private simpleName;
        private typeName;
        private canonicalName;
        private packageName;
        private compoundName;
        private typeId;
        private arrayLiterals;
        private sequentialId;
        static nextSequentialId: number;
        /**
         * Not publicly instantiable.
         *
         * @skip
         */
        constructor();
        desiredAssertionStatus(): boolean;
        private ensureNamesAreInitialized();
        getCanonicalName(): string;
        getComponentType(): any;
        getEnumConstants(): T[];
        getName(): string;
        getSimpleName(): string;
        getSuperclass(): any;
        isArray(): boolean;
        isEnum(): boolean;
        isInterface(): boolean;
        isPrimitive(): boolean;
        toString(): string;
        /**
         * Used by Enum to allow getSuperclass() to be pruned.
         */
        getEnumSuperclass(): any;
    }
}
declare namespace java.lang {
    /**
     * Indicates that a class implements <code>clone()</code>.
     */
    interface Cloneable {
    }
}
declare namespace java.lang {
    /**
     * An interface used a basis for implementing custom ordering. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Comparable.html">[Sun
     * docs]</a>
     *
     * @param <T> the type to compare to.
     */
    interface Comparable<T> {
        compareTo(other?: any): any;
    }
}
declare namespace java.lang {
    /**
     * A program element annotated &#64;Deprecated is one that programmers are
     * discouraged from using, typically because it is dangerous, or because a
     * better alternative exists. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Deprecated.html">[Sun
     * docs]</a>
     */
    interface Deprecated {
    }
}
declare namespace java.lang {
    /**
     * The first-class representation of an enumeration.
     *
     * @param <E>
     */
    abstract class Enum<E extends java.lang.Enum<E>> implements java.lang.Comparable<E>, java.io.Serializable {
        static valueOf<T extends java.lang.Enum<T>>(enumType?: any, name?: any): any;
        static createValueOfMap<T extends java.lang.Enum<T>>(enumConstants: T[]): Object;
        static valueOf$jsweet_lang_Object$java_lang_String<T extends java.lang.Enum<T>>(map: Object, name: string): T;
        private static get0<T>(map, name);
        private static invokeValueOf<T>(enumValueOfFunc, name);
        private static put0<T>(map, name, value);
        private __name;
        private __ordinal;
        constructor(name: string, ordinal: number);
        compareTo(other?: any): any;
        getDeclaringClass(): any;
        name(): string;
        ordinal(): number;
        toString(): string;
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Exception.html">the
     * official Java API doc</a> for details.
     */
    class Exception extends Error {
        constructor(message?: any, cause?: any, enableSuppression?: any, writableStackTrace?: any);
    }
}
declare namespace java.lang {
    /**
     * Used to declare interfaces which must have a single abstract method.
     */
    interface FunctionalInterface {
    }
}
declare namespace java.lang {
    /**
     * Allows an instance of a class implementing this interface to be used in the
     * foreach statement.
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html">
     * the official Java API doc</a> for details.
     *
     * @param <T> type of returned iterator
     */
    interface Iterable<T> {
        iterator(): java.util.Iterator<T>;
        forEach(action: (p1: any) => void): any;
    }
}
declare namespace java.lang {
    /**
     * Indicates that a method definition is intended to override a declaration from
     * a superclass. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Override.html">[Sun
     * docs]</a>
     */
    interface Override {
    }
}
declare namespace java.lang.reflect {
    /**
     * This interface makes {@link java.lang.reflect.Type} available to GWT clients.
     *
     * @see java.lang.reflect.Type
     */
    interface Type {
    }
}
declare namespace java.lang {
    /**
     * Encapsulates an action for later execution. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Runnable.html">[Sun
     * docs]</a>
     *
     * <p>
     * This interface is provided only for JRE compatibility. GWT does not support
     * multithreading.
     * </p>
     */
    interface Runnable {
        run(): any;
    }
}
declare namespace java.lang {
    /**
     * Claims to the compiler that the annotation target does nothing potentially unsafe
     * to its varargs argument.
     */
    interface SafeVarargs {
    }
}
declare namespace java.lang {
    /**
     * Included for hosted mode source compatibility. Partially implemented
     *
     * @skip
     */
    class StackTraceElement implements java.io.Serializable {
        private className;
        private fileName;
        private lineNumber;
        private methodName;
        constructor(className?: any, methodName?: any, fileName?: any, lineNumber?: any);
        getClassName(): string;
        getFileName(): string;
        getLineNumber(): number;
        getMethodName(): string;
        equals(other: any): boolean;
        hashCode(): number;
        toString(): string;
    }
}
declare namespace java.lang {
    /**
     * Indicates that the named compiler warnings should be suppressed in the
     * annotated element (and in all program elements contained in the annotated
     * element). <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/SuppressWarnings.html">[Sun
     * docs]</a>
     */
    interface SuppressWarnings {
        value(): string[];
    }
}
declare namespace java.lang {
    /**
     * Thrown to indicate that the Java Virtual Machine is broken or has
     * run out of resources necessary for it to continue operating.
     *
     *
     * @author  Frank Yellin
     * @since   JDK1.0
     */
    abstract class VirtualMachineError extends Error {
        static serialVersionUID: number;
        /**
         * Constructs a {@code VirtualMachineError} with the specified
         * detail message and cause.  <p>Note that the detail message
         * associated with {@code cause} is <i>not</i> automatically
         * incorporated in this error's detail message.
         *
         * @param  message the detail message (which is saved for later retrieval
         * by the {@link #getMessage()} method).
         * @param  cause the cause (which is saved for later retrieval by the
         * {@link #getCause()} method).  (A {@code null} value is
         * permitted, and indicates that the cause is nonexistent or
         * unknown.)
         * @since  1.8
         */
        constructor(message?: any, cause?: any);
    }
}
declare namespace java.lang {
    /**
     * For JRE compatibility.
     */
    class Void {
        /**
         * Not instantiable.
         */
        constructor();
    }
}
declare namespace java.nio.charset {
    /**
     * A minimal emulation of {@link Charset}.
     */
    abstract class Charset implements java.lang.Comparable<Charset> {
        static availableCharsets(): java.util.SortedMap<string, Charset>;
        static forName(charsetName: string): Charset;
        static createLegalCharsetNameRegex(): RegExp;
        private __name;
        constructor(name: string, aliasesIgnored: string[]);
        name(): string;
        compareTo(that?: any): any;
        hashCode(): number;
        equals(o: any): boolean;
        toString(): string;
    }
    namespace Charset {
        class AvailableCharsets {
            static CHARSETS: java.util.SortedMap<string, java.nio.charset.Charset>;
        }
    }
}
declare namespace java.security {
    /**
     * Message Digest Service Provider Interface - <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/security/MessageDigestSpi.html">[Sun's
     * docs]</a>.
     */
    abstract class MessageDigestSpi {
        engineDigest$(): number[];
        engineDigest(buf?: any, offset?: any, len?: any): any;
        engineGetDigestLength(): number;
        abstract engineReset(): any;
        engineUpdate$byte(input: number): void;
        engineUpdate(input?: any, offset?: any, len?: any): any;
    }
}
declare namespace java.util {
    /**
     * Skeletal implementation of the Collection interface. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/AbstractCollection.html">[Sun
     * docs]</a>
     *
     * @param <E> the element type.
     */
    abstract class AbstractCollection<E> implements java.util.Collection<E> {
        forEach(action: (p1: any) => void): void;
        constructor();
        add(index?: any, element?: any): any;
        add$java_lang_Object(o: E): boolean;
        addAll(index?: any, c?: any): any;
        addAll$java_util_Collection(c: java.util.Collection<any>): boolean;
        clear(): void;
        contains(o: any): boolean;
        containsAll(c: java.util.Collection<any>): boolean;
        isEmpty(): boolean;
        abstract iterator(): java.util.Iterator<E>;
        remove(index?: any): any;
        remove$java_lang_Object(o: any): boolean;
        removeAll(c: java.util.Collection<any>): boolean;
        retainAll(c: java.util.Collection<any>): boolean;
        abstract size(): number;
        toArray$(): any[];
        toArray<T>(a?: any): any;
        toString(): string;
        private advanceToFind(o, remove);
    }
}
declare namespace java.util {
    /**
     * Basic {@link Map.Entry} implementation that implements hashCode, equals, and
     * toString.
     */
    abstract class AbstractMapEntry<K, V> implements java.util.Map.Entry<K, V> {
        abstract getKey(): any;
        abstract getValue(): any;
        abstract setValue(value: any): any;
        equals(other: any): boolean;
        /**
         * Calculate the hash code using Sun's specified algorithm.
         */
        hashCode(): number;
        toString(): string;
        constructor();
    }
}
declare namespace java.util {
    /**
     * General-purpose interface for storing collections of objects. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Collection.html">[Sun
     * docs]</a>
     *
     * @param <E> element type
     */
    interface Collection<E> extends java.lang.Iterable<E> {
        add(index?: any, element?: any): any;
        addAll(index?: any, c?: any): any;
        clear(): any;
        contains(o: any): boolean;
        containsAll(c: Collection<any>): boolean;
        equals(o: any): boolean;
        hashCode(): number;
        isEmpty(): boolean;
        iterator(): java.util.Iterator<E>;
        remove(index?: any): any;
        removeAll(c: Collection<any>): boolean;
        retainAll(c: Collection<any>): boolean;
        size(): number;
        toArray<T>(a?: any): any;
    }
}
declare namespace java.util {
    /**
     * An interface used a basis for implementing custom ordering. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Comparator.html">[Sun
     * docs]</a>
     *
     * @param <T> the type to be compared.
     */
    interface Comparator<T> {
        compare(o1?: any, o2?: any): any;
        equals(other: any): boolean;
    }
}
declare namespace java.util {
    class Comparators {
        /**
         * Compares two Objects according to their <i>natural ordering</i>.
         *
         * @see java.lang.Comparable
         */
        static NATURAL: java.util.Comparator<any>;
        static NATURAL_$LI$(): java.util.Comparator<any>;
        /**
         * Returns the natural Comparator.
         * <p>
         * Example:
         *
         * <pre>Comparator&lt;String&gt; compareString = Comparators.natural()</pre>
         *
         * @return the natural Comparator
         */
        static natural<T>(): java.util.Comparator<T>;
    }
    namespace Comparators {
        class NaturalComparator implements java.util.Comparator<any> {
            compare(o1: any, o2: any): number;
            constructor();
        }
    }
}
declare namespace java.util {
    /**
     * Represents a date and time.
     */
    class Date implements java.lang.Cloneable, java.lang.Comparable<Date>, java.io.Serializable {
        static parse(s: string): number;
        static UTC(year: number, month: number, date: number, hrs: number, min: number, sec: number): number;
        /**
         * Ensure a number is displayed with two digits.
         *
         * @return a two-character base 10 representation of the number
         */
        static padTwo(number: number): string;
        /**
         * JavaScript Date instance.
         */
        private jsdate;
        static jsdateClass(): Object;
        constructor(year?: any, month?: any, date?: any, hrs?: any, min?: any, sec?: any);
        after(ts?: any): any;
        after$java_util_Date(when: Date): boolean;
        before(ts?: any): any;
        before$java_util_Date(when: Date): boolean;
        clone(): any;
        compareTo(other?: any): any;
        equals(ts?: any): any;
        equals$java_lang_Object(obj: any): boolean;
        getDate(): number;
        getDay(): number;
        getHours(): number;
        getMinutes(): number;
        getMonth(): number;
        getSeconds(): number;
        getTime(): number;
        getTimezoneOffset(): number;
        getYear(): number;
        hashCode(): number;
        setDate(date: number): void;
        setHours(hours: number): void;
        setMinutes(minutes: number): void;
        setMonth(month: number): void;
        setSeconds(seconds: number): void;
        setTime(time: number): void;
        setYear(year: number): void;
        toGMTString(): string;
        toLocaleString(): string;
        toString(): string;
        static ONE_HOUR_IN_MILLISECONDS: number;
        static ONE_HOUR_IN_MILLISECONDS_$LI$(): number;
        /**
         * Detects if the requested time falls into a non-existent time range due to
         * local time advancing into daylight savings time or is ambiguous due to
         * going out of daylight savings. If so, adjust accordingly.
         */
        fixDaylightSavings(requestedHours: number): void;
    }
    namespace Date {
        /**
         * Encapsulates static data to avoid Date itself having a static
         * initializer.
         */
        class StringData {
            static DAYS: string[];
            static DAYS_$LI$(): string[];
            static MONTHS: string[];
            static MONTHS_$LI$(): string[];
        }
    }
}
declare namespace java.util {
    /**
     * A collection designed for holding elements prior to processing. <a
     * href="http://docs.oracle.com/javase/6/docs/api/java/util/Deque.html">Deque</a>
     *
     * @param <E> element type.
     */
    interface Deque<E> extends java.util.Queue<E> {
        addFirst(e: E): any;
        addLast(e: E): any;
        descendingIterator(): java.util.Iterator<E>;
        getFirst(): E;
        getLast(): E;
        offerFirst(e: E): boolean;
        offerLast(e: E): boolean;
        peekFirst(): E;
        peekLast(): E;
        pollFirst(): E;
        pollLast(): E;
        pop(): E;
        push(e: E): any;
        removeFirst(): E;
        removeFirstOccurrence(o: any): boolean;
        removeLast(): E;
        removeLastOccurrence(o: any): boolean;
    }
}
declare namespace java.util {
    /**
     * An interface to generate a series of elements, one at a time. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Enumeration.html">[Sun
     * docs]</a>
     *
     * @param <E> the type being enumerated.
     */
    interface Enumeration<E> {
        hasMoreElements(): boolean;
        nextElement(): E;
    }
}
declare namespace java.util {
    /**
     * A tag interface that other "listener" interfaces can extend to indicate their
     * adherence to the observer pattern.
     */
    interface EventListener {
    }
}
declare namespace java.util {
    /**
     * Available as a superclass of event objects.
     */
    class EventObject {
        source: any;
        constructor(source: any);
        getSource(): any;
    }
}
declare namespace java.util {
    /**
     * A simple wrapper around JavaScriptObject to provide {@link java.util.Map}-like semantics for any
     * key type.
     * <p>
     * Implementation notes:
     * <p>
     * A key's hashCode is the index in backingMap which should contain that key. Since several keys may
     * have the same hash, each value in hashCodeMap is actually an array containing all entries whose
     * keys share the same hash.
     */
    class InternalHashCodeMap<K, V> implements java.lang.Iterable<Map.Entry<K, V>> {
        forEach(action: (p1: any) => void): void;
        private backingMap;
        private host;
        private __size;
        constructor(host: java.util.AbstractHashMap<K, V>);
        put(key: K, value: V): V;
        remove(key: any): V;
        getEntry(key: any): java.util.Map.Entry<K, V>;
        private findEntryInChain(key, chain);
        size(): number;
        iterator(): java.util.Iterator<Map.Entry<K, V>>;
        private getChainOrEmpty(hashCode);
        private newEntryChain();
        private unsafeCastToArray(arr);
        /**
         * Returns hash code of the key as calculated by {@link AbstractHashMap#getHashCode(Object)} but
         * also handles null keys as well.
         */
        private hash(key);
    }
    namespace InternalHashCodeMap {
        class InternalHashCodeMap$0 implements java.util.Iterator<java.util.Map.Entry<any, any>> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            chains: java.util.InternalJsMap.Iterator<any>;
            itemIndex: number;
            chain: Map.Entry<any, any>[];
            lastEntry: Map.Entry<any, any>;
            hasNext(): boolean;
            next(): Map.Entry<any, any>;
            remove(): void;
            constructor(__parent: any);
        }
    }
}
declare namespace java.util {
    class InternalJsMap<V> {
        get$int(key: number): V;
        get(key?: any): any;
        set$int$java_lang_Object(key: number, value: V): void;
        set(key?: any, value?: any): any;
        delete$int(key: number): void;
        delete(key?: any): any;
        entries(): InternalJsMap.Iterator<V>;
    }
    namespace InternalJsMap {
        class Iterator<V> {
            next(): InternalJsMap.IteratorEntry<V>;
        }
        class IteratorEntry<V> {
            value: any[];
            done: boolean;
            constructor();
        }
        class JsHelper {
            static delete$java_util_InternalJsMap$int(obj: java.util.InternalJsMap<any>, key: number): void;
            static delete(obj?: any, key?: any): any;
        }
    }
}
declare namespace java.util {
    /**
     * A factory to create JavaScript Map instances.
     */
    class InternalJsMapFactory {
        static jsMapCtor: any;
        static jsMapCtor_$LI$(): any;
        private static getJsMapConstructor();
        static newJsMap<V>(): java.util.InternalJsMap<V>;
        constructor();
    }
}
declare namespace java.util {
    /**
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html">
     * the official Java API doc</a> for details.
     *
     * @param <E> element type
     */
    interface Iterator<E> {
        hasNext(): boolean;
        next(): E;
        forEachRemaining(consumer?: any): any;
        remove(): any;
    }
}
declare namespace java.util {
    /**
     * Represents a sequence of objects. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/List.html">[Sun docs]</a>
     *
     * @param <E> element type
     */
    interface List<E> extends java.util.Collection<E> {
        add(index?: any, element?: any): any;
        addAll(index?: any, c?: any): any;
        clear(): any;
        contains(o: any): boolean;
        containsAll(c: java.util.Collection<any>): boolean;
        equals(o: any): boolean;
        get(index: number): E;
        hashCode(): number;
        indexOf(o?: any, index?: any): any;
        isEmpty(): boolean;
        iterator(): java.util.Iterator<E>;
        lastIndexOf(o?: any, index?: any): any;
        listIterator(from?: any): any;
        remove(index?: any): any;
        removeAll(c: java.util.Collection<any>): boolean;
        retainAll(c: java.util.Collection<any>): boolean;
        set(index: number, element: E): E;
        size(): number;
        subList(fromIndex: number, toIndex: number): List<E>;
        toArray<T>(array?: any): any;
    }
}
declare namespace java.util {
    /**
     * Uses Java 1.5 ListIterator for documentation. The methods hasNext, next, and
     * remove are repeated to allow the specialized ListIterator documentation to be
     * associated with them. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/ListIterator.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    interface ListIterator<E> extends java.util.Iterator<E> {
        add(o: E): any;
        hasNext(): boolean;
        hasPrevious(): boolean;
        next(): E;
        nextIndex(): number;
        previous(): E;
        previousIndex(): number;
        remove(): any;
        set(o: E): any;
    }
}
declare namespace java.util {
    /**
     * A very simple emulation of Locale for shared-code patterns like
     * {@code String.toUpperCase(Locale.US)}.
     * <p>
     * Note: Any changes to this class should put into account the assumption that
     * was made in rest of the JRE emulation.
     */
    class Locale {
        static ROOT: Locale;
        static ROOT_$LI$(): Locale;
        static ENGLISH: Locale;
        static ENGLISH_$LI$(): Locale;
        static US: Locale;
        static US_$LI$(): Locale;
        static defaultLocale: Locale;
        static defaultLocale_$LI$(): Locale;
        /**
         * Returns an instance that represents the browser's default locale (not
         * necessarily the one defined by 'gwt.locale').
         */
        static getDefault(): Locale;
        constructor();
    }
    namespace Locale {
        class RootLocale extends java.util.Locale {
            toString(): string;
        }
        class EnglishLocale extends java.util.Locale {
            toString(): string;
        }
        class USLocale extends java.util.Locale {
            toString(): string;
        }
        class DefaultLocale extends java.util.Locale {
            toString(): string;
        }
    }
}
declare namespace java.util.logging {
    /**
     * An emulation of the java.util.logging.Formatter class. See
     * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/Formatter.html">
     * The Java API doc for details</a>
     */
    abstract class Formatter {
        abstract format(record: java.util.logging.LogRecord): string;
        formatMessage(record: java.util.logging.LogRecord): string;
    }
}
declare namespace java.util.logging {
    /**
     * An emulation of the java.util.logging.Handler class. See
     * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/Handler.html">
     * The Java API doc for details</a>
     */
    abstract class Handler {
        private formatter;
        private level;
        abstract close(): any;
        abstract flush(): any;
        getFormatter(): java.util.logging.Formatter;
        getLevel(): java.util.logging.Level;
        isLoggable(record: java.util.logging.LogRecord): boolean;
        abstract publish(record: java.util.logging.LogRecord): any;
        setFormatter(newFormatter: java.util.logging.Formatter): void;
        setLevel(newLevel: java.util.logging.Level): void;
        constructor();
    }
}
declare namespace java.util.logging {
    /**
     * An emulation of the java.util.logging.Level class. See
     * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/Level.html">
     * The Java API doc for details</a>
     */
    class Level implements java.io.Serializable {
        static ALL: Level;
        static ALL_$LI$(): Level;
        static CONFIG: Level;
        static CONFIG_$LI$(): Level;
        static FINE: Level;
        static FINE_$LI$(): Level;
        static FINER: Level;
        static FINER_$LI$(): Level;
        static FINEST: Level;
        static FINEST_$LI$(): Level;
        static INFO: Level;
        static INFO_$LI$(): Level;
        static OFF: Level;
        static OFF_$LI$(): Level;
        static SEVERE: Level;
        static SEVERE_$LI$(): Level;
        static WARNING: Level;
        static WARNING_$LI$(): Level;
        static parse(name: string): Level;
        constructor();
        getName(): string;
        intValue(): number;
        toString(): string;
    }
    namespace Level {
        class LevelAll extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
        class LevelConfig extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
        class LevelFine extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
        class LevelFiner extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
        class LevelFinest extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
        class LevelInfo extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
        class LevelOff extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
        class LevelSevere extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
        class LevelWarning extends java.util.logging.Level {
            getName(): string;
            intValue(): number;
            constructor();
        }
    }
}
declare namespace java.util.logging {
    /**
     * An emulation of the java.util.logging.LogManager class. See
     * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/LogManger.html">
     * The Java API doc for details</a>
     */
    class LogManager {
        static singleton: LogManager;
        static getLogManager(): LogManager;
        private loggerMap;
        constructor();
        addLogger(logger: java.util.logging.Logger): boolean;
        getLogger(name: string): java.util.logging.Logger;
        getLoggerNames(): java.util.Enumeration<string>;
        /**
         * Helper function to add a logger when we have already determined that it
         * does not exist.  When we add a logger, we recursively add all of it's
         * ancestors. Since loggers do not get removed, logger creation is cheap,
         * and there are not usually too many loggers in an ancestry chain,
         * this is a simple way to ensure that the parent/child relationships are
         * always correctly set up.
         */
        private addLoggerAndEnsureParents(logger);
        private addLoggerImpl(logger);
        /**
         * Helper function to create a logger if it does not exist since the public
         * APIs for getLogger and addLogger make it difficult to use those functions
         * for this.
         */
        ensureLogger(name: string): java.util.logging.Logger;
    }
}
declare namespace java.util.logging {
    /**
     * An emulation of the java.util.logging.LogRecord class. See
     * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/LogRecord.html">
     * The Java API doc for details</a>
     */
    class LogRecord implements java.io.Serializable {
        private level;
        private loggerName;
        private msg;
        private thrown;
        private millis;
        constructor(level?: any, msg?: any);
        getLevel(): java.util.logging.Level;
        getLoggerName(): string;
        getMessage(): string;
        getMillis(): number;
        getThrown(): Error;
        setLevel(newLevel: java.util.logging.Level): void;
        setLoggerName(newName: string): void;
        setMessage(newMessage: string): void;
        setMillis(newMillis: number): void;
        setThrown(newThrown: Error): void;
    }
}
declare namespace java.util {
    /**
     * Abstract interface for maps.
     *
     * @param <K> key type.
     * @param <V> value type.
     */
    interface Map<K, V> {
        clear(): any;
        containsKey(key: any): boolean;
        containsValue(value: any): boolean;
        entrySet(): java.util.Set<Map.Entry<K, V>>;
        equals(o: any): boolean;
        get(key: any): V;
        hashCode(): number;
        isEmpty(): boolean;
        keySet(): java.util.Set<K>;
        put(key?: any, value?: any): any;
        putAll(t: Map<any, any>): any;
        remove(key: any): V;
        size(): number;
        values(): java.util.Collection<V>;
    }
    namespace Map {
        /**
         * Represents an individual map entry.
         */
        interface Entry<K, V> {
            equals(o: any): boolean;
            getKey(): K;
            getValue(): V;
            hashCode(): number;
            setValue(value: V): V;
        }
    }
}
declare namespace java.util {
    /**
     * Sorted map providing additional query operations and views.
     *
     * @param <K> key type.
     * @param <V> value type.
     */
    interface NavigableMap<K, V> extends java.util.SortedMap<K, V> {
        ceilingEntry(key: K): java.util.Map.Entry<K, V>;
        ceilingKey(key: K): K;
        descendingKeySet(): java.util.NavigableSet<K>;
        descendingMap(): NavigableMap<K, V>;
        firstEntry(): java.util.Map.Entry<K, V>;
        floorEntry(key: K): java.util.Map.Entry<K, V>;
        floorKey(key: K): K;
        headMap(toKey?: any, inclusive?: any): any;
        higherEntry(key: K): java.util.Map.Entry<K, V>;
        higherKey(key: K): K;
        lastEntry(): java.util.Map.Entry<K, V>;
        lowerEntry(key: K): java.util.Map.Entry<K, V>;
        lowerKey(key: K): K;
        navigableKeySet(): java.util.NavigableSet<K>;
        pollFirstEntry(): java.util.Map.Entry<K, V>;
        pollLastEntry(): java.util.Map.Entry<K, V>;
        subMap(fromKey?: any, fromInclusive?: any, toKey?: any, toInclusive?: any): any;
        tailMap(fromKey?: any, inclusive?: any): any;
    }
}
declare namespace java.util {
    /**
     * A {@code SortedSet} with more flexible queries.
     *
     * @param <E> element type.
     */
    interface NavigableSet<E> extends java.util.SortedSet<E> {
        ceiling(e: E): E;
        descendingIterator(): java.util.Iterator<E>;
        descendingSet(): NavigableSet<E>;
        floor(e: E): E;
        headSet(toElement?: any, inclusive?: any): any;
        higher(e: E): E;
        lower(e: E): E;
        pollFirst(): E;
        pollLast(): E;
        subSet(fromElement?: any, fromInclusive?: any, toElement?: any, toInclusive?: any): any;
        tailSet(fromElement?: any, inclusive?: any): any;
    }
}
declare namespace java.util {
    /**
     * See <a
     * href="http://docs.oracle.com/javase/7/docs/api/java/util/Objects.html">the
     * official Java API doc</a> for details.
     */
    class Objects {
        constructor();
        static compare<T>(a: T, b: T, c: java.util.Comparator<any>): number;
        static deepEquals(a: any, b: any): boolean;
        static equals(a: any, b: any): boolean;
        static hashCode(o: any): number;
        static hash(...values: any[]): number;
        static isNull(obj: any): boolean;
        static nonNull(obj: any): boolean;
        static requireNonNull$java_lang_Object<T>(obj: T): T;
        static requireNonNull<T>(obj?: any, message?: any): any;
        static requireNonNull$java_lang_Object$java_util_function_Supplier<T>(obj: T, messageSupplier: () => string): T;
        static toString$java_lang_Object(o: any): string;
        static toString(o?: any, nullDefault?: any): any;
    }
}
declare namespace java.util {
    /**
     * Implementation of the observable class.
     */
    class Observable {
        private changed;
        private obs;
        /**
         * Construct an Observable with zero Observers.
         */
        constructor();
        /**
         * Adds an observer to the set of observers for this object, provided that
         * it is not the same as some observer already in the set. The order in
         * which notifications will be delivered to multiple observers is not
         * specified. See the class comment.
         *
         * @param o
         * an observer to be added.
         * @throws NullPointerException
         * if the parameter o is null.
         */
        addObserver(o: java.util.Observer): void;
        /**
         * Deletes an observer from the set of observers of this object. Passing
         * <CODE>null</CODE> to this method will have no effect.
         *
         * @param o
         * the observer to be deleted.
         */
        deleteObserver(o: java.util.Observer): void;
        /**
         * If this object has changed, as indicated by the <code>hasChanged</code>
         * method, then notify all of its observers and then call the
         * <code>clearChanged</code> method to indicate that this object has no
         * longer changed.
         * <p>
         * Each observer has its <code>update</code> method called with two
         * arguments: this observable object and the <code>arg</code> argument.
         *
         * @param arg
         * any object.
         * @see java.util.Observable#clearChanged()
         * @see java.util.Observable#hasChanged()
         * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
         */
        notifyObservers(arg?: any): void;
        /**
         * Clears the observer list so that this object no longer has any observers.
         */
        deleteObservers(): void;
        /**
         * Marks this <tt>Observable</tt> object as having been changed; the
         * <tt>hasChanged</tt> method will now return <tt>true</tt>.
         */
        setChanged(): void;
        /**
         * Indicates that this object has no longer changed, or that it has already
         * notified all of its observers of its most recent change, so that the
         * <tt>hasChanged</tt> method will now return <tt>false</tt>. This method is
         * called automatically by the <code>notifyObservers</code> methods.
         *
         * @see java.util.Observable#notifyObservers()
         * @see java.util.Observable#notifyObservers(java.lang.Object)
         */
        clearChanged(): void;
        /**
         * Tests if this object has changed.
         *
         * @return <code>true</code> if and only if the <code>setChanged</code>
         * method has been called more recently than the
         * <code>clearChanged</code> method on this object;
         * <code>false</code> otherwise.
         * @see java.util.Observable#clearChanged()
         * @see java.util.Observable#setChanged()
         */
        hasChanged(): boolean;
        /**
         * Returns the number of observers of this <tt>Observable</tt> object.
         *
         * @return the number of observers of this object.
         */
        countObservers(): number;
    }
}
declare namespace java.util {
    /**
     * Implementation of the observer interface
     */
    interface Observer {
        /**
         * This method is called whenever the observed object is changed. An
         * application calls an <tt>Observable</tt> object's
         * <code>notifyObservers</code> method to have all the object's
         * observers notified of the change.
         *
         * @param   o     the observable object.
         * @param   arg   an argument passed to the <code>notifyObservers</code>
         * method.
         */
        update(o: java.util.Observable, arg: any): any;
    }
}
declare namespace java.util {
    /**
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html">
     * the official Java API doc</a> for details.
     *
     * @param <T> type of the wrapped reference
     */
    class Optional<T> {
        static empty<T>(): Optional<T>;
        static of<T>(value: T): Optional<T>;
        static ofNullable<T>(value: T): Optional<T>;
        static EMPTY: Optional<any>;
        static EMPTY_$LI$(): Optional<any>;
        private ref;
        constructor(ref?: any);
        isPresent(): boolean;
        get(): T;
        ifPresent(consumer: (p1: any) => void): void;
        filter(predicate: (p1: any) => boolean): Optional<T>;
        map<U>(mapper: (p1: any) => any): Optional<U>;
        flatMap<U>(mapper: (p1: any) => Optional<U>): Optional<U>;
        orElse(other: T): T;
        orElseGet(other: () => any): T;
        orElseThrow<X extends Error>(exceptionSupplier: () => any): T;
        equals(obj: any): boolean;
        hashCode(): number;
        toString(): string;
    }
}
declare namespace java.util {
    /**
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/OptionalDouble.html">
     * the official Java API doc</a> for details.
     */
    class OptionalDouble {
        static empty(): OptionalDouble;
        static of(value: number): OptionalDouble;
        static EMPTY: OptionalDouble;
        static EMPTY_$LI$(): OptionalDouble;
        private ref;
        private present;
        constructor(value?: any);
        isPresent(): boolean;
        getAsDouble(): number;
        ifPresent(consumer: (number) => void): void;
        orElse(other: number): number;
        orElseGet(other: () => number): number;
        orElseThrow<X extends Error>(exceptionSupplier: () => X): number;
        equals(obj: any): boolean;
        hashCode(): number;
        toString(): string;
    }
}
declare namespace java.util {
    /**
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/OptionalInt.html">
     * the official Java API doc</a> for details.
     */
    class OptionalInt {
        static empty(): OptionalInt;
        static of(value: number): OptionalInt;
        static EMPTY: OptionalInt;
        static EMPTY_$LI$(): OptionalInt;
        private ref;
        private present;
        constructor(value?: any);
        isPresent(): boolean;
        getAsInt(): number;
        ifPresent(consumer: (number) => void): void;
        orElse(other: number): number;
        orElseGet(other: () => number): number;
        orElseThrow<X extends Error>(exceptionSupplier: () => X): number;
        equals(obj: any): boolean;
        hashCode(): number;
        toString(): string;
    }
}
declare namespace java.util {
    /**
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/OptionalLong.html">
     * the official Java API doc</a> for details.
     */
    class OptionalLong {
        static empty(): OptionalLong;
        static of(value: number): OptionalLong;
        static EMPTY: OptionalLong;
        static EMPTY_$LI$(): OptionalLong;
        private ref;
        private present;
        constructor(value?: any);
        isPresent(): boolean;
        getAsLong(): number;
        ifPresent(consumer: (number) => void): void;
        orElse(other: number): number;
        orElseGet(other: () => number): number;
        orElseThrow<X extends Error>(exceptionSupplier: () => X): number;
        equals(obj: any): boolean;
        hashCode(): number;
        toString(): string;
    }
}
declare namespace java.util {
    /**
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/PrimitiveIterator.html">
     * the official Java API doc</a> for details.
     *
     * @param <T> element type
     * @param <C> consumer type
     */
    interface PrimitiveIterator<T, C> extends java.util.Iterator<T> {
    }
    namespace PrimitiveIterator {
        /**
         * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/PrimitiveIterator.OfDouble.html">
         * the official Java API doc</a> for details.
         */
        interface OfDouble extends java.util.PrimitiveIterator<number, (number) => void> {
            nextDouble(): number;
            next(): number;
            forEachRemaining(consumer?: any): any;
        }
        /**
         * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/PrimitiveIterator.OfInt.html">
         * the official Java API doc</a> for details.
         */
        interface OfInt extends java.util.PrimitiveIterator<number, (number) => void> {
            nextInt(): number;
            next(): number;
            forEachRemaining(consumer?: any): any;
        }
        /**
         * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/PrimitiveIterator.OfLong.html">
         * the official Java API doc</a> for details.
         */
        interface OfLong extends java.util.PrimitiveIterator<number, (number) => void> {
            nextLong(): number;
            next(): number;
            forEachRemaining(consumer?: any): any;
        }
    }
}
declare namespace java.util {
    /**
     * A collection designed for holding elements prior to processing. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Queue.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    interface Queue<E> extends java.util.Collection<E> {
        element(): E;
        offer(o: E): boolean;
        peek(): E;
        poll(): E;
        remove(index?: any): any;
    }
}
declare namespace java.util {
    /**
     * This class provides methods that generates pseudo-random numbers of different
     * types, such as {@code int}, {@code long}, {@code double}, and {@code float}.
     * It follows the algorithms specified in the JRE javadoc.
     *
     * This emulated version of Random is not serializable.
     */
    class Random {
        static __static_initialized: boolean;
        static __static_initialize(): void;
        static multiplierHi: number;
        static multiplierLo: number;
        static twoToThe24: number;
        static twoToThe31: number;
        static twoToThe32: number;
        static twoToTheMinus24: number;
        static twoToTheMinus26: number;
        static twoToTheMinus31: number;
        static twoToTheMinus53: number;
        static twoToTheXMinus24: number[];
        static twoToTheXMinus24_$LI$(): number[];
        static twoToTheXMinus48: number[];
        static twoToTheXMinus48_$LI$(): number[];
        /**
         * A value used to avoid two random number generators produced at the same
         * time having the same seed.
         */
        static uniqueSeed: number;
        static __static_initializer_0(): void;
        /**
         * The boolean value indicating if the second Gaussian number is available.
         */
        private haveNextNextGaussian;
        /**
         * The second Gaussian generated number.
         */
        private nextNextGaussian;
        /**
         * The high 24 bits of the 48=bit seed value.
         */
        private seedhi;
        /**
         * The low 24 bits of the 48=bit seed value.
         */
        private seedlo;
        /**
         * Construct a random generator with the given {@code seed} as the initial
         * state.
         *
         * @param seed the seed that will determine the initial state of this random
         * number generator.
         * @see #setSeed
         */
        constructor(seed?: any);
        /**
         * Returns the next pseudo-random, uniformly distributed {@code boolean} value
         * generated by this generator.
         *
         * @return a pseudo-random, uniformly distributed boolean value.
         */
        nextBoolean(): boolean;
        /**
         * Modifies the {@code byte} array by a random sequence of {@code byte}s
         * generated by this random number generator.
         *
         * @param buf non-null array to contain the new random {@code byte}s.
         * @see #next
         */
        nextBytes(buf: number[]): void;
        /**
         * Generates a normally distributed random {@code double} number between 0.0
         * inclusively and 1.0 exclusively.
         *
         * @return a random {@code double} in the range [0.0 - 1.0)
         * @see #nextFloat
         */
        nextDouble(): number;
        /**
         * Generates a normally distributed random {@code float} number between 0.0
         * inclusively and 1.0 exclusively.
         *
         * @return float a random {@code float} number between [0.0 and 1.0)
         * @see #nextDouble
         */
        nextFloat(): number;
        /**
         * Pseudo-randomly generates (approximately) a normally distributed {@code
         * double} value with mean 0.0 and a standard deviation value of {@code 1.0}
         * using the <i>polar method<i> of G. E. P. Box, M. E. Muller, and G.
         * Marsaglia, as described by Donald E. Knuth in <i>The Art of Computer
         * Programming, Volume 2: Seminumerical Algorithms</i>, section 3.4.1,
         * subsection C, algorithm P.
         *
         * @return a random {@code double}
         * @see #nextDouble
         */
        nextGaussian(): number;
        /**
         * Generates a uniformly distributed 32-bit {@code int} value from the random
         * number sequence.
         *
         * @return a uniformly distributed {@code int} value.
         * @see java.lang.Integer#MAX_VALUE
         * @see java.lang.Integer#MIN_VALUE
         * @see #next
         * @see #nextLong
         */
        nextInt$(): number;
        /**
         * Returns a new pseudo-random {@code int} value which is uniformly
         * distributed between 0 (inclusively) and the value of {@code n}
         * (exclusively).
         *
         * @param n the exclusive upper border of the range [0 - n).
         * @return a random {@code int}.
         */
        nextInt(n?: any): any;
        /**
         * Generates a uniformly distributed 64-bit integer value from the random
         * number sequence.
         *
         * @return 64-bit random integer.
         * @see java.lang.Integer#MAX_VALUE
         * @see java.lang.Integer#MIN_VALUE
         * @see #next
         * @see #nextInt()
         * @see #nextInt(int)
         */
        nextLong(): number;
        /**
         * Modifies the seed a using linear congruential formula presented in <i>The
         * Art of Computer Programming, Volume 2</i>, Section 3.2.1.
         *
         * @param seed the seed that alters the state of the random number generator.
         * @see #next
         * @see #Random()
         * @see #Random(long)
         */
        setSeed$long(seed: number): void;
        /**
         * Returns a pseudo-random uniformly distributed {@code int} value of the
         * number of bits specified by the argument {@code bits} as described by
         * Donald E. Knuth in <i>The Art of Computer Programming, Volume 2:
         * Seminumerical Algorithms</i>, section 3.2.1.
         *
         * @param bits number of bits of the returned value.
         * @return a pseudo-random generated int number.
         * @see #nextBytes
         * @see #nextDouble
         * @see #nextFloat
         * @see #nextInt()
         * @see #nextInt(int)
         * @see #nextGaussian
         * @see #nextLong
         */
        next(bits: number): number;
        private nextInternal(bits);
        setSeed(seedhi?: any, seedlo?: any): any;
    }
}
declare namespace java.util {
    /**
     * Indicates that a data structure supports constant-time random access to its
     * contained objects.
     */
    interface RandomAccess {
    }
}
declare namespace java.util {
    /**
     * Represents a set of unique objects. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Set.html">[Sun docs]</a>
     *
     * @param <E> element type.
     */
    interface Set<E> extends java.util.Collection<E> {
        add(index?: any, element?: any): any;
        addAll(index?: any, c?: any): any;
        clear(): any;
        contains(o: any): boolean;
        containsAll(c: java.util.Collection<any>): boolean;
        equals(o: any): boolean;
        hashCode(): number;
        isEmpty(): boolean;
        iterator(): java.util.Iterator<E>;
        remove(index?: any): any;
        removeAll(c: java.util.Collection<any>): boolean;
        retainAll(c: java.util.Collection<any>): boolean;
        size(): number;
        toArray<T>(a?: any): any;
    }
}
declare namespace java.util {
    /**
     * A map with ordering. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/SortedMap.html">[Sun
     * docs]</a>
     *
     * @param <K> key type.
     * @param <V> value type.
     */
    interface SortedMap<K, V> extends java.util.Map<K, V> {
        comparator(): java.util.Comparator<any>;
        firstKey(): K;
        headMap(toKey?: any, inclusive?: any): any;
        lastKey(): K;
        subMap(fromKey?: any, fromInclusive?: any, toKey?: any, toInclusive?: any): any;
        tailMap(fromKey?: any, inclusive?: any): any;
    }
}
declare namespace java.util {
    /**
     * A set known to be in ascending order. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/SortedSet.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    interface SortedSet<E> extends java.util.Set<E> {
        comparator(): java.util.Comparator<any>;
        first(): E;
        headSet(toElement?: any, inclusive?: any): any;
        last(): E;
        subSet(fromElement?: any, fromInclusive?: any, toElement?: any, toInclusive?: any): any;
        tailSet(fromElement?: any, inclusive?: any): any;
    }
}
declare namespace java.util {
    /**
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/StringJoiner.html">
     * the official Java API doc</a> for details.
     */
    class StringJoiner {
        private delimiter;
        private prefix;
        private suffix;
        private builder;
        private emptyValue;
        constructor(delimiter: string, prefix?: string, suffix?: string);
        add(newElement: string): StringJoiner;
        length(): number;
        merge(other: StringJoiner): StringJoiner;
        setEmptyValue(emptyValue: string): StringJoiner;
        toString(): string;
        private initBuilderOrAddDelimiter();
    }
}
declare namespace javaemul.internal.annotations {
    /**
     * An annotation to mark another annotation as a compiler hint.
     */
    interface CompilerHint {
    }
}
declare namespace javaemul.internal.annotations {
    /**
     * An annotation to mark a given method as not inlineable.
     * <p>
     * Internal SDK use only, might change or disappear at any time.
     */
    interface DoNotInline {
    }
}
declare namespace javaemul.internal.annotations {
    /**
     * An annotation to mark a given method as not inlineable.
     * <p>
     * Internal SDK use only, might change or disappear at any time.
     */
    interface ForceInline {
    }
}
declare namespace javaemul.internal.annotations {
    /**
     * A simple of a GwtIncompatible annotation for internal emulation use.
     */
    interface GwtIncompatible {
        value(): string;
    }
}
declare namespace javaemul.internal.annotations {
    /**
     * An annotation to mark a given method as side-effect free.
     * <p>
     * Internal SDK use only, might change or disappear at any time.
     */
    interface HasNoSideEffects {
    }
}
declare namespace javaemul.internal.annotations {
    /**
     * An annotation to mark a given method as being specialized. If the specified
     * parameters and return context match of a JMethodCall, then the call
     * is retargeted at the specialized version.
     */
    interface SpecializeMethod {
        /**
         * List of parameter types, matched via assignability.
         */
        params(): any[];
        /**
         * List of return types to match, or null if you don't care.
         */
        returns(): any;
        /**
         * The name of the method to target. It must have a signature matching to the {@link #params()}.
         */
        target(): string;
    }
    namespace SpecializeMethod {
        /**
         * Represents a type that matches any type, even void.
         */
        interface ANY {
        }
    }
}
declare namespace javaemul.internal {
    /**
     * Provides utilities to perform operations on Arrays.
     */
    class ArrayHelper {
        static ARRAY_PROCESS_BATCH_SIZE: number;
        static clone<T>(array: T[], fromIndex: number, toIndex: number): T[];
        /**
         * Unlike clone, this method returns a copy of the array that is not type
         * marked. This is only safe for temp arrays as returned array will not do
         * any type checks.
         */
        static unsafeClone(array: any, fromIndex: number, toIndex: number): any[];
        static createFrom<T>(array: T[], length: number): T[];
        private static createNativeArray(length);
        static getLength(array: any): number;
        static setLength(array: any, length: number): void;
        static removeFrom(array: any, index: number, deleteCount: number): void;
        static insertTo$java_lang_Object$int$java_lang_Object(array: any, index: number, value: any): void;
        static insertTo(array?: any, index?: any, values?: any): any;
        static copy(src: any, srcOfs: number, dest: any, destOfs: number, len: number, overwrite?: boolean): void;
    }
}
declare namespace javaemul.internal {
    /**
     * A utility to provide array stamping. Provided as a separate class to simplify
     * super-source.
     */
    class ArrayStamper {
        static stampJavaTypeInfo<T>(array: any, referenceType: T[]): T[];
    }
}
declare namespace javaemul.internal {
    /**
     * Wraps native <code>boolean</code> as an object.
     */
    class BooleanHelper implements java.lang.Comparable<BooleanHelper>, java.io.Serializable {
        static FALSE: boolean;
        static TRUE: boolean;
        static TYPE: typeof Boolean;
        static TYPE_$LI$(): typeof Boolean;
        static compare(x: boolean, y: boolean): number;
        static hashCode(value: boolean): number;
        static logicalAnd(a: boolean, b: boolean): boolean;
        static logicalOr(a: boolean, b: boolean): boolean;
        static logicalXor(a: boolean, b: boolean): boolean;
        static parseBoolean(s: string): boolean;
        static toString(x: boolean): string;
        static valueOf$boolean(b: boolean): boolean;
        static valueOf(s?: any): any;
        booleanValue(): boolean;
        private static unsafeCast(value);
        compareTo(b?: any): any;
        equals(o: any): boolean;
        hashCode(): number;
        toString(): string;
        constructor();
    }
}
declare namespace javaemul.internal {
    /**
     * Wraps a native <code>char</code> as an object.
     *
     * TODO(jat): many of the classification methods implemented here are not
     * correct in that they only handle ASCII characters, and many other methods are
     * not currently implemented. I think the proper approach is to introduce * a
     * deferred binding parameter which substitutes an implementation using a
     * fully-correct Unicode character database, at the expense of additional data
     * being downloaded. That way developers that need the functionality can get it
     * without those who don't need it paying for it.
     *
     * <pre>
     * The following methods are still not implemented -- most would require Unicode
     * character db to be useful:
     * - digit / is* / to*(int codePoint)
     * - isDefined(char)
     * - isIdentifierIgnorable(char)
     * - isJavaIdentifierPart(char)
     * - isJavaIdentifierStart(char)
     * - isJavaLetter(char) -- deprecated, so probably not
     * - isJavaLetterOrDigit(char) -- deprecated, so probably not
     * - isISOControl(char)
     * - isMirrored(char)
     * - isSpaceChar(char)
     * - isTitleCase(char)
     * - isUnicodeIdentifierPart(char)
     * - isUnicodeIdentifierStart(char)
     * - getDirectionality(*)
     * - getNumericValue(*)
     * - getType(*)
     * - reverseBytes(char) -- any use for this at all in the browser?
     * - toTitleCase(*)
     * - all the category constants for classification
     *
     * The following do not properly handle characters outside of ASCII:
     * - digit(char c, int radix)
     * - isDigit(char c)
     * - isLetter(char c)
     * - isLetterOrDigit(char c)
     * - isLowerCase(char c)
     * - isUpperCase(char c)
     * </pre>
     */
    class CharacterHelper implements java.lang.Comparable<CharacterHelper>, java.io.Serializable {
        static TYPE: typeof String;
        static TYPE_$LI$(): typeof String;
        static MIN_RADIX: number;
        static MAX_RADIX: number;
        static MIN_VALUE: string;
        static MAX_VALUE: string;
        static MIN_SURROGATE: string;
        static MAX_SURROGATE: string;
        static MIN_LOW_SURROGATE: string;
        static MAX_LOW_SURROGATE: string;
        static MIN_HIGH_SURROGATE: string;
        static MAX_HIGH_SURROGATE: string;
        static MIN_SUPPLEMENTARY_CODE_POINT: number;
        static MIN_CODE_POINT: number;
        static MAX_CODE_POINT: number;
        static SIZE: number;
        static charCount(codePoint: number): number;
        static codePointAt$char_A$int(a: string[], index: number): number;
        static codePointAt(a?: any, index?: any, limit?: any): any;
        static codePointAt$java_lang_CharSequence$int(seq: string, index: number): number;
        static codePointBefore$char_A$int(a: string[], index: number): number;
        static codePointBefore(a?: any, index?: any, start?: any): any;
        static codePointBefore$java_lang_CharSequence$int(cs: string, index: number): number;
        static codePointCount(a?: any, offset?: any, count?: any): any;
        static codePointCount$java_lang_CharSequence$int$int(seq: string, beginIndex: number, endIndex: number): number;
        static compare(x: string, y: string): number;
        static digit(c: string, radix: number): number;
        static getNumericValue(ch: string): number;
        static forDigit(digit?: any, radix?: any): any;
        /**
         * @skip
         *
         * public for shared implementation with Arrays.hashCode
         */
        static hashCode(c: string): number;
        static isDigit(c: string): boolean;
        static digitRegex(): RegExp;
        static isHighSurrogate(ch: string): boolean;
        static isLetter(c: string): boolean;
        static leterRegex(): RegExp;
        static isLetterOrDigit(c: string): boolean;
        static leterOrDigitRegex(): RegExp;
        static isLowerCase(c: string): boolean;
        static isLowSurrogate(ch: string): boolean;
        /**
         * Deprecated - see isWhitespace(char).
         */
        static isSpace(c: string): boolean;
        static isWhitespace(ch?: any): any;
        static isWhitespace$int(codePoint: number): boolean;
        static whitespaceRegex(): RegExp;
        static isSupplementaryCodePoint(codePoint: number): boolean;
        static isSurrogatePair(highSurrogate: string, lowSurrogate: string): boolean;
        static isUpperCase(c: string): boolean;
        static isValidCodePoint(codePoint: number): boolean;
        static offsetByCodePoints(a?: any, start?: any, count?: any, index?: any, codePointOffset?: any): any;
        static offsetByCodePoints$java_lang_CharSequence$int$int(seq: string, index: number, codePointOffset: number): number;
        static toChars$int(codePoint: number): string[];
        static toChars(codePoint?: any, dst?: any, dstIndex?: any): any;
        static toCodePoint(highSurrogate: string, lowSurrogate: string): number;
        static toLowerCase(c: string): string;
        static toString(x: string): string;
        static toUpperCase(c: string): string;
        static valueOf(c: string): CharacterHelper;
        static codePointAt$java_lang_CharSequence$int$int(cs: string, index: number, limit: number): number;
        static codePointBefore$java_lang_CharSequence$int$int(cs: string, index: number, start: number): number;
        /**
         * Shared implementation with {@link LongHelper#toString}.
         *
         * @skip
         */
        static forDigit$int(digit: number): string;
        /**
         * Computes the high surrogate character of the UTF16 representation of a
         * non-BMP code point. See {@link getLowSurrogate}.
         *
         * @param codePoint
         * requested codePoint, required to be >=
         * MIN_SUPPLEMENTARY_CODE_POINT
         * @return high surrogate character
         */
        static getHighSurrogate(codePoint: number): string;
        /**
         * Computes the low surrogate character of the UTF16 representation of a
         * non-BMP code point. See {@link getHighSurrogate}.
         *
         * @param codePoint
         * requested codePoint, required to be >=
         * MIN_SUPPLEMENTARY_CODE_POINT
         * @return low surrogate character
         */
        static getLowSurrogate(codePoint: number): string;
        private value;
        constructor(value: string);
        charValue(): string;
        compareTo(c?: any): any;
        equals(o: any): boolean;
        hashCode(): number;
        toString(): string;
    }
    namespace CharacterHelper {
        /**
         * Use nested class to avoid clinit on outer.
         */
        class BoxedValues {
            static boxedValues: javaemul.internal.CharacterHelper[];
            static boxedValues_$LI$(): javaemul.internal.CharacterHelper[];
        }
    }
}
declare namespace javaemul.internal {
    /**
     * Private implementation class for GWT. This API should not be
     * considered public or stable.
     */
    class Coercions {
        /**
         * Coerce js int to 32 bits.
         * Trick related to JS and lack of integer rollover.
         * {@see com.google.gwt.lang.Cast#narrow_int}
         */
        static ensureInt(value: number): number;
        constructor();
    }
}
declare namespace javaemul.internal {
    /**
     * Simple Helper class to return Date.now.
     */
    class DateUtil {
        /**
         * Returns the numeric value corresponding to the current time -
         * the number of milliseconds elapsed since 1 January 1970 00:00:00 UTC.
         */
        static now(): number;
    }
}
declare var Map: Object;
declare namespace javaemul.internal {
    /**
     * Contains logics for calculating hash codes in JavaScript.
     */
    class HashCodes {
        static sNextHashId: number;
        static HASH_CODE_PROPERTY: string;
        static hashCodeForString(s: string): number;
        static getIdentityHashCode(o: any): number;
        static getObjectIdentityHashCode(o: any): number;
        /**
         * Called from JSNI. Do not change this implementation without updating:
         * <ul>
         * <li>{@link com.google.gwt.user.client.rpc.impl.SerializerBase}</li>
         * </ul>
         */
        private static getNextHashId();
    }
}
declare namespace javaemul.internal {
    class JreHelper {
        static LOG10E: number;
        static LOG10E_$LI$(): number;
    }
}
declare namespace javaemul.internal {
    /**
     * Provides an interface for simple JavaScript idioms that can not be expressed in Java.
     */
    class JsUtils {
        static getInfinity(): number;
        static isUndefined(value: any): boolean;
        static unsafeCastToString(string: any): string;
        static setPropertySafe(map: any, key: string, value: any): void;
        static getIntProperty(map: any, key: string): number;
        static setIntProperty(map: any, key: string, value: number): void;
        static typeOf(o: any): string;
    }
}
declare namespace javaemul.internal {
    /**
     * A helper class for long comparison.
     */
    class LongCompareHolder {
        static getLongComparator(): any;
    }
}
declare namespace javaemul.internal {
    /**
     * Math utility methods and constants.
     */
    class MathHelper {
        static EPSILON: number;
        static EPSILON_$LI$(): number;
        static MAX_VALUE: number;
        static MAX_VALUE_$LI$(): number;
        static MIN_VALUE: number;
        static MIN_VALUE_$LI$(): number;
        static nextDown(x: number): number;
        static ulp(x: number): number;
        static nextUp(x: number): number;
        static E: number;
        static PI: number;
        static PI_OVER_180: number;
        static PI_OVER_180_$LI$(): number;
        static PI_UNDER_180: number;
        static PI_UNDER_180_$LI$(): number;
        static abs$double(x: number): number;
        static abs$float(x: number): number;
        static abs(x?: any): any;
        static abs$long(x: number): number;
        static acos(x: number): number;
        static asin(x: number): number;
        static atan(x: number): number;
        static atan2(y: number, x: number): number;
        static cbrt(x: number): number;
        static ceil(x: number): number;
        static copySign$double$double(magnitude: number, sign: number): number;
        static copySign(magnitude?: any, sign?: any): any;
        static cos(x: number): number;
        static cosh(x: number): number;
        static exp(x: number): number;
        static expm1(d: number): number;
        static floor(x: number): number;
        static hypot(x: number, y: number): number;
        static log(x: number): number;
        static log10(x: number): number;
        static log1p(x: number): number;
        static max$double$double(x: number, y: number): number;
        static max$float$float(x: number, y: number): number;
        static max(x?: any, y?: any): any;
        static max$long$long(x: number, y: number): number;
        static min$double$double(x: number, y: number): number;
        static min$float$float(x: number, y: number): number;
        static min(x?: any, y?: any): any;
        static min$long$long(x: number, y: number): number;
        static pow(x: number, exp: number): number;
        static random(): number;
        static rint(d: number): number;
        static round$double(x: number): number;
        static round(x?: any): any;
        private static unsafeCastToInt(d);
        static scalb$double$int(d: number, scaleFactor: number): number;
        static scalb(f?: any, scaleFactor?: any): any;
        static signum$double(d: number): number;
        static signum(f?: any): any;
        static sin(x: number): number;
        static sinh(x: number): number;
        static sqrt(x: number): number;
        static tan(x: number): number;
        static tanh(x: number): number;
        static toDegrees(x: number): number;
        static toRadians(x: number): number;
        static IEEEremainder(f1: number, f2: number): number;
    }
}
declare namespace javaemul.internal {
    /**
     * Abstract base class for numeric wrapper classes.
     */
    abstract class NumberHelper implements java.io.Serializable {
        /**
         * Stores a regular expression object to verify the format of float values.
         */
        static floatRegex: RegExp;
        /**
         * @skip
         *
         * This function will determine the radix that the string is expressed
         * in based on the parsing rules defined in the Javadocs for
         * Integer.decode() and invoke __parseAndValidateInt.
         */
        static __decodeAndValidateInt(s: string, lowerBound: number, upperBound: number): number;
        static __decodeNumberString(s: string): NumberHelper.__Decode;
        /**
         * @skip
         *
         * This function contains common logic for parsing a String as a
         * floating- point number and validating the range.
         */
        static __parseAndValidateDouble(s: string): number;
        /**
         * @skip
         *
         * This function contains common logic for parsing a String in a given
         * radix and validating the result.
         */
        static __parseAndValidateInt(s: string, radix: number, lowerBound: number, upperBound: number): number;
        /**
         * @skip
         *
         * This function contains common logic for parsing a String in a given
         * radix and validating the result.
         */
        static __parseAndValidateLong(s: string, radix: number): number;
        /**
         * @skip
         *
         * @param str
         * @return {@code true} if the string matches the float format,
         * {@code false} otherwise
         */
        static __isValidDouble(str: string): boolean;
        static createFloatRegex(): RegExp;
        byteValue(): number;
        abstract doubleValue(): number;
        abstract floatValue(): number;
        abstract intValue(): number;
        abstract longValue(): number;
        shortValue(): number;
        constructor();
    }
    namespace NumberHelper {
        class __Decode {
            payload: string;
            radix: number;
            constructor(radix: number, payload: string);
        }
        /**
         * Use nested class to avoid clinit on outer.
         */
        class __ParseLong {
            static __static_initialized: boolean;
            static __static_initialize(): void;
            /**
             * The number of digits (excluding minus sign and leading zeros) to
             * process at a time. The largest value expressible in maxDigits digits
             * as well as the factor radix^maxDigits must be strictly less than
             * 2^31.
             */
            static maxDigitsForRadix: number[];
            static maxDigitsForRadix_$LI$(): number[];
            /**
             * A table of values radix*maxDigitsForRadix[radix].
             */
            static maxDigitsRadixPower: number[];
            static maxDigitsRadixPower_$LI$(): number[];
            /**
             * The largest number of digits (excluding minus sign and leading zeros)
             * that can fit into a long for a given radix between 2 and 36,
             * inclusive.
             */
            static maxLengthForRadix: number[];
            static maxLengthForRadix_$LI$(): number[];
            /**
             * A table of floor(MAX_VALUE / maxDigitsRadixPower).
             */
            static maxValueForRadix: number[];
            static maxValueForRadix_$LI$(): number[];
            static __static_initializer_0(): void;
        }
    }
}
declare namespace javaemul.internal {
    class ObjectHelper {
        static clone(obj: any): any;
    }
}
declare namespace javaemul.internal {
    /**
     * Hashcode caching for strings.
     */
    class StringHashCache {
        /**
         * The "old" cache; it will be dumped when front is full.
         */
        static back: any;
        static back_$LI$(): any;
        /**
         * Tracks the number of entries in front.
         */
        static count: number;
        /**
         * The "new" cache; it will become back when it becomes full.
         */
        static front: any;
        static front_$LI$(): any;
        /**
         * Pulled this number out of thin air.
         */
        static MAX_CACHE: number;
        static getHashCode(str: string): number;
        private static compute(str);
        private static increment();
        private static getProperty(map, key);
        private static createNativeObject();
        private static unsafeCastToInt(o);
    }
}
/**
 * Declares equals and hashCode on JavaScript objects, for compilation.
 */
interface Object {
    equals(object: Object): boolean;
    hashCode(): number;
}
declare namespace sun.awt.geom {
    class ChainEnd {
        head: sun.awt.geom.CurveLink;
        tail: sun.awt.geom.CurveLink;
        partner: ChainEnd;
        etag: number;
        constructor(first: sun.awt.geom.CurveLink, partner: ChainEnd);
        getChain(): sun.awt.geom.CurveLink;
        setOtherEnd(partner: ChainEnd): void;
        getPartner(): ChainEnd;
        linkTo(that: ChainEnd): sun.awt.geom.CurveLink;
        addLink(newlink: sun.awt.geom.CurveLink): void;
        getX(): number;
    }
}
declare namespace sun.awt.geom {
    abstract class Crossings {
        static debug: boolean;
        limit: number;
        yranges: number[];
        xlo: number;
        ylo: number;
        xhi: number;
        yhi: number;
        constructor(xlo: number, ylo: number, xhi: number, yhi: number);
        getXLo(): number;
        getYLo(): number;
        getXHi(): number;
        getYHi(): number;
        abstract record(ystart: number, yend: number, direction: number): any;
        print(): void;
        isEmpty(): boolean;
        abstract covers(ystart: number, yend: number): boolean;
        static findCrossings(curves?: any, xlo?: any, ylo?: any, xhi?: any, yhi?: any): any;
        static findCrossings$java_awt_geom_PathIterator$double$double$double$double(pi: java.awt.geom.PathIterator, xlo: number, ylo: number, xhi: number, yhi: number): Crossings;
        accumulateLine$double$double$double$double(x0: number, y0: number, x1: number, y1: number): boolean;
        accumulateLine(x0?: any, y0?: any, x1?: any, y1?: any, direction?: any): any;
        private tmp;
        accumulateQuad(x0: number, y0: number, coords: number[]): boolean;
        accumulateCubic(x0: number, y0: number, coords: number[]): boolean;
    }
    namespace Crossings {
        class EvenOdd extends sun.awt.geom.Crossings {
            constructor(xlo: number, ylo: number, xhi: number, yhi: number);
            covers(ystart: number, yend: number): boolean;
            record(ystart: number, yend: number, direction: number): void;
        }
        class NonZero extends sun.awt.geom.Crossings {
            crosscounts: number[];
            constructor(xlo: number, ylo: number, xhi: number, yhi: number);
            covers(ystart: number, yend: number): boolean;
            remove(cur: number): void;
            insert(cur: number, lo: number, hi: number, dir: number): void;
            record(ystart: number, yend: number, direction: number): void;
        }
    }
}
declare namespace sun.awt.geom {
    abstract class Curve {
        static INCREASING: number;
        static DECREASING: number;
        direction: number;
        static insertMove(curves: java.util.Vector<any>, x: number, y: number): void;
        static insertLine(curves: java.util.Vector<any>, x0: number, y0: number, x1: number, y1: number): void;
        static insertQuad(curves: java.util.Vector<any>, x0: number, y0: number, coords: number[]): void;
        static insertCubic(curves: java.util.Vector<any>, x0: number, y0: number, coords: number[]): void;
        /**
         * Calculates the number of times the given path crosses the ray extending
         * to the right from (px,py). If the point lies on a part of the path, then
         * no crossings are counted for that intersection. +1 is added for each
         * crossing where the Y coordinate is increasing -1 is added for each
         * crossing where the Y coordinate is decreasing The return value is the sum
         * of all crossings for every segment in the path. The path must start with
         * a SEG_MOVETO, otherwise an exception is thrown. The caller must check
         * p[xy] for NaN values. The caller may also reject infinite p[xy] values as
         * well.
         */
        static pointCrossingsForPath(pi: java.awt.geom.PathIterator, px: number, py: number): number;
        /**
         * Calculates the number of times the line from (x0,y0) to (x1,y1) crosses
         * the ray extending to the right from (px,py). If the point lies on the
         * line, then no crossings are recorded. +1 is returned for a crossing where
         * the Y coordinate is increasing -1 is returned for a crossing where the Y
         * coordinate is decreasing
         */
        static pointCrossingsForLine(px: number, py: number, x0: number, y0: number, x1: number, y1: number): number;
        /**
         * Calculates the number of times the quad from (x0,y0) to (x1,y1) crosses
         * the ray extending to the right from (px,py). If the point lies on a part
         * of the curve, then no crossings are counted for that intersection. the
         * level parameter should be 0 at the top-level call and will count up for
         * each recursion level to prevent infinite recursion +1 is added for each
         * crossing where the Y coordinate is increasing -1 is added for each
         * crossing where the Y coordinate is decreasing
         */
        static pointCrossingsForQuad(px: number, py: number, x0: number, y0: number, xc: number, yc: number, x1: number, y1: number, level: number): number;
        /**
         * Calculates the number of times the cubic from (x0,y0) to (x1,y1) crosses
         * the ray extending to the right from (px,py). If the point lies on a part
         * of the curve, then no crossings are counted for that intersection. the
         * level parameter should be 0 at the top-level call and will count up for
         * each recursion level to prevent infinite recursion +1 is added for each
         * crossing where the Y coordinate is increasing -1 is added for each
         * crossing where the Y coordinate is decreasing
         */
        static pointCrossingsForCubic(px: number, py: number, x0: number, y0: number, xc0: number, yc0: number, xc1: number, yc1: number, x1: number, y1: number, level: number): number;
        /**
         * The rectangle intersection test counts the number of times that the path
         * crosses through the shadow that the rectangle projects to the right
         * towards (x => +INFINITY).
         *
         * During processing of the path it actually counts every time the path
         * crosses either or both of the top and bottom edges of that shadow. If the
         * path enters from the top, the count is incremented. If it then exits back
         * through the top, the same way it came in, the count is decremented and
         * there is no impact on the winding count. If, instead, the path exits out
         * the bottom, then the count is incremented again and a full pass through
         * the shadow is indicated by the winding count having been incremented by
         * 2.
         *
         * Thus, the winding count that it accumulates is actually double the real
         * winding count. Since the path is continuous, the final answer should be a
         * multiple of 2, otherwise there is a logic error somewhere.
         *
         * If the path ever has a direct hit on the rectangle, then a special value
         * is returned. This special value terminates all ongoing accumulation on up
         * through the call chain and ends up getting returned to the calling
         * function which can then produce an answer directly. For intersection
         * tests, the answer is always "true" if the path intersects the rectangle.
         * For containment tests, the answer is always "false" if the path
         * intersects the rectangle. Thus, no further processing is ever needed if
         * an intersection occurs.
         */
        static RECT_INTERSECTS: number;
        /**
         * Accumulate the number of times the path crosses the shadow extending to
         * the right of the rectangle. See the comment for the RECT_INTERSECTS
         * constant for more complete details. The return value is the sum of all
         * crossings for both the top and bottom of the shadow for every segment in
         * the path, or the special value RECT_INTERSECTS if the path ever enters
         * the interior of the rectangle. The path must start with a SEG_MOVETO,
         * otherwise an exception is thrown. The caller must check r[xy]{min,max}
         * for NaN values.
         */
        static rectCrossingsForPath(pi: java.awt.geom.PathIterator, rxmin: number, rymin: number, rxmax: number, rymax: number): number;
        /**
         * Accumulate the number of times the line crosses the shadow extending to
         * the right of the rectangle. See the comment for the RECT_INTERSECTS
         * constant for more complete details.
         */
        static rectCrossingsForLine(crossings: number, rxmin: number, rymin: number, rxmax: number, rymax: number, x0: number, y0: number, x1: number, y1: number): number;
        /**
         * Accumulate the number of times the quad crosses the shadow extending to
         * the right of the rectangle. See the comment for the RECT_INTERSECTS
         * constant for more complete details.
         */
        static rectCrossingsForQuad(crossings: number, rxmin: number, rymin: number, rxmax: number, rymax: number, x0: number, y0: number, xc: number, yc: number, x1: number, y1: number, level: number): number;
        /**
         * Accumulate the number of times the cubic crosses the shadow extending to
         * the right of the rectangle. See the comment for the RECT_INTERSECTS
         * constant for more complete details.
         */
        static rectCrossingsForCubic(crossings: number, rxmin: number, rymin: number, rxmax: number, rymax: number, x0: number, y0: number, xc0: number, yc0: number, xc1: number, yc1: number, x1: number, y1: number, level: number): number;
        constructor(direction: number);
        getDirection(): number;
        getWithDirection(direction: number): Curve;
        static round(v: number): number;
        static orderof(x1: number, x2: number): number;
        static signeddiffbits(y1: number, y2: number): number;
        static diffbits(y1: number, y2: number): number;
        static prev(v: number): number;
        static next(v: number): number;
        toString(): string;
        controlPointString(): string;
        abstract getOrder(): number;
        abstract getXTop(): number;
        abstract getYTop(): number;
        abstract getXBot(): number;
        abstract getYBot(): number;
        abstract getXMin(): number;
        abstract getXMax(): number;
        abstract getX0(): number;
        abstract getY0(): number;
        abstract getX1(): number;
        abstract getY1(): number;
        abstract XforY(y: number): number;
        abstract TforY(y: number): number;
        abstract XforT(t: number): number;
        abstract YforT(t: number): number;
        abstract dXforT(t: number, deriv: number): number;
        abstract dYforT(t: number, deriv: number): number;
        abstract nextVertical(t0: number, t1: number): number;
        crossingsFor(x: number, y: number): number;
        accumulateCrossings(c: sun.awt.geom.Crossings): boolean;
        abstract enlarge(r: java.awt.geom.Rectangle2D): any;
        getSubCurve$double$double(ystart: number, yend: number): Curve;
        abstract getReversedCurve(): Curve;
        getSubCurve(ystart?: any, yend?: any, dir?: any): any;
        compareTo(that: Curve, yrange: number[]): number;
        static TMIN: number;
        findIntersect(that: Curve, yrange: number[], ymin: number, slevel: number, tlevel: number, s0: number, xs0: number, ys0: number, s1: number, xs1: number, ys1: number, t0: number, xt0: number, yt0: number, t1: number, xt1: number, yt1: number): boolean;
        refineTforY(t0: number, yt0: number, y0: number): number;
        fairlyClose(v1: number, v2: number): boolean;
        abstract getSegment(coords: number[]): number;
    }
}
declare namespace sun.awt.geom {
    class CurveLink {
        curve: sun.awt.geom.Curve;
        ytop: number;
        ybot: number;
        etag: number;
        next: CurveLink;
        constructor(curve: sun.awt.geom.Curve, ystart: number, yend: number, etag: number);
        absorb$sun_awt_geom_CurveLink(link: CurveLink): boolean;
        absorb(curve?: any, ystart?: any, yend?: any, etag?: any): any;
        isEmpty(): boolean;
        getCurve(): sun.awt.geom.Curve;
        getSubCurve(): sun.awt.geom.Curve;
        getMoveto(): sun.awt.geom.Curve;
        getXTop(): number;
        getYTop(): number;
        getXBot(): number;
        getYBot(): number;
        getX(): number;
        getEdgeTag(): number;
        setNext(link: CurveLink): void;
        getNext(): CurveLink;
    }
}
declare namespace sun.awt.geom {
    class Edge {
        static INIT_PARTS: number;
        static GROW_PARTS: number;
        curve: sun.awt.geom.Curve;
        ctag: number;
        etag: number;
        activey: number;
        equivalence: number;
        constructor(c: sun.awt.geom.Curve, ctag: number, etag?: number);
        getCurve(): sun.awt.geom.Curve;
        getCurveTag(): number;
        getEdgeTag(): number;
        setEdgeTag(etag: number): void;
        getEquivalence(): number;
        setEquivalence(eq: number): void;
        private lastEdge;
        private lastResult;
        private lastLimit;
        compareTo(other: Edge, yrange: number[]): number;
        record(yend: number, etag: number): void;
        isActiveFor(y: number, etag: number): boolean;
        toString(): string;
    }
}
declare namespace sun.awt.geom {
    interface PathConsumer2D {
        /**
         * @see java.awt.geom.Path2D.Float.moveTo
         */
        moveTo(x: number, y: number): any;
        /**
         * @see java.awt.geom.Path2D.Float.lineTo
         */
        lineTo(x: number, y: number): any;
        /**
         * @see java.awt.geom.Path2D.Float.quadTo
         */
        quadTo(x1: number, y1: number, x2: number, y2: number): any;
        /**
         * @see java.awt.geom.Path2D.Float.curveTo
         */
        curveTo(x1: number, y1: number, x2: number, y2: number, x3: number, y3: number): any;
        /**
         * @see java.awt.geom.Path2D.Float.closePath
         */
        closePath(): any;
        /**
         * Called after the last segment of the last subpath when the iteration of
         * the path segments is completely done. This method serves to trigger the
         * end of path processing in the consumer that would normally be triggered
         * when a {@link java.awt.geom.PathIterator PathIterator} returns
         * {@code true} from its {@code done} method.
         */
        pathDone(): any;
        /**
         * If a given PathConsumer performs all or most of its work natively then it
         * can return a (non-zero) pointer to a native function vector that defines
         * C functions for all of the above methods. The specific pointer it returns
         * is a pointer to a PathConsumerVec structure as defined in the include
         * file src/share/native/sun/java2d/pipe/PathConsumer2D.h
         *
         * @return a native pointer to a PathConsumerVec structure.
         */
        getNativeConsumer(): number;
    }
}
declare namespace test {
    class Test {
        static assertEquals(o1: any, o2: any): void;
        static assertTrue(b: boolean): void;
        static assertFalse(b: boolean): void;
        static test(): void;
        static testList(): void;
        static testSet(): void;
        static testMap(): void;
        static testString(): void;
        static testIO(): void;
        static testAwtGeom(): void;
    }
}
declare namespace java.awt {
    class Button extends java.awt.Component implements java.awt.HTMLComponent {
        button: HTMLButtonElement;
        actionListener: java.awt.event.ActionListener;
        actionCommand: string;
        label: string;
        background: java.awt.Color;
        constructor(label: string);
        bind(id: string): void;
        init(): void;
        private initActionListener();
        getHTMLElement(): HTMLElement;
        addActionListener(actionListener: java.awt.event.ActionListener): void;
        setBackground(background: java.awt.Color): void;
    }
}
declare namespace java.awt {
    class TextField extends java.awt.Component implements java.awt.HTMLComponent {
        input: HTMLInputElement;
        actionListener: java.awt.event.ActionListener;
        constructor(cols: number);
        init(): void;
        private initActionListener();
        bind(id: string): void;
        getHTMLElement(): HTMLElement;
        addActionListener(actionListener: java.awt.event.ActionListener): void;
        setText(text: string): void;
        getText(): string;
    }
}
declare namespace java.awt {
    /**
     * The <code>Dimension</code> class encapsulates the width and
     * height of a component (in integer precision) in a single object.
     * The class is
     * associated with certain properties of components. Several methods
     * defined by the <code>Component</code> class and the
     * <code>LayoutManager</code> interface return a
     * <code>Dimension</code> object.
     * <p>
     * Normally the values of <code>width</code>
     * and <code>height</code> are non-negative integers.
     * The constructors that allow you to create a dimension do
     * not prevent you from setting a negative value for these properties.
     * If the value of <code>width</code> or <code>height</code> is
     * negative, the behavior of some methods defined by other objects is
     * undefined.
     *
     * @author      Sami Shaio
     * @author      Arthur van Hoff
     * @see         java.awt.Component
     * @see         java.awt.LayoutManager
     * @since       1.0
     */
    class Dimension extends java.awt.geom.Dimension2D implements java.io.Serializable {
        /**
         * The width dimension; negative values can be used.
         *
         * @serial
         * @see #getSize
         * @see #setSize
         * @since 1.0
         */
        width: number;
        /**
         * The height dimension; negative values can be used.
         *
         * @serial
         * @see #getSize
         * @see #setSize
         * @since 1.0
         */
        height: number;
        static serialVersionUID: number;
        /**
         * Constructs a <code>Dimension</code> and initializes
         * it to the specified width and specified height.
         *
         * @param width the specified width
         * @param height the specified height
         */
        constructor(width?: any, height?: any);
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        getWidth(): number;
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        getHeight(): number;
        /**
         * Sets the size of this <code>Dimension</code> object to
         * the specified width and height in double precision.
         * Note that if <code>width</code> or <code>height</code>
         * are larger than <code>Integer.MAX_VALUE</code>, they will
         * be reset to <code>Integer.MAX_VALUE</code>.
         *
         * @param width  the new width for the <code>Dimension</code> object
         * @param height the new height for the <code>Dimension</code> object
         * @since 1.2
         */
        setSize$double$double(width: number, height: number): void;
        /**
         * Gets the size of this <code>Dimension</code> object.
         * This method is included for completeness, to parallel the
         * <code>getSize</code> method defined by <code>Component</code>.
         *
         * @return   the size of this dimension, a new instance of
         * <code>Dimension</code> with the same width and height
         * @see      java.awt.Dimension#setSize
         * @see      java.awt.Component#getSize
         * @since    1.1
         */
        getSize(): Dimension;
        /**
         * Sets the size of this <code>Dimension</code> object to the specified size.
         * This method is included for completeness, to parallel the
         * <code>setSize</code> method defined by <code>Component</code>.
         * @param    d  the new size for this <code>Dimension</code> object
         * @see      java.awt.Dimension#getSize
         * @see      java.awt.Component#setSize
         * @since    1.1
         */
        setSize$java_awt_Dimension(d: Dimension): void;
        /**
         * Sets the size of this <code>Dimension</code> object
         * to the specified width and height.
         * This method is included for completeness, to parallel the
         * <code>setSize</code> method defined by <code>Component</code>.
         *
         * @param    width   the new width for this <code>Dimension</code> object
         * @param    height  the new height for this <code>Dimension</code> object
         * @see      java.awt.Dimension#getSize
         * @see      java.awt.Component#setSize
         * @since    1.1
         */
        setSize(width?: any, height?: any): any;
        /**
         * Checks whether two dimension objects have equal values.
         */
        equals(obj: any): boolean;
        /**
         * Returns the hash code for this <code>Dimension</code>.
         *
         * @return    a hash code for this <code>Dimension</code>
         */
        hashCode(): number;
        /**
         * Returns a string representation of the values of this
         * <code>Dimension</code> object's <code>height</code> and
         * <code>width</code> fields. This method is intended to be used only
         * for debugging purposes, and the content and format of the returned
         * string may vary between implementations. The returned string may be
         * empty but may not be <code>null</code>.
         *
         * @return  a string representation of this <code>Dimension</code>
         * object
         */
        toString(): string;
    }
}
declare namespace java.awt.geom {
    /**
     * The {@code Path2D} class provides a simple, yet flexible shape which
     * represents an arbitrary geometric path. It can fully represent any path which
     * can be iterated by the {@link PathIterator} interface including all of its
     * segment types and winding rules and it implements all of the basic hit
     * testing methods of the {@link Shape} interface.
     * <p>
     * Use {@link Path2D.Float} when dealing with data that can be represented and
     * used with floating point precision. Use {@link Path2D.Double} for data that
     * requires the accuracy or range of double precision.
     * <p>
     * {@code Path2D} provides exactly those facilities required for basic
     * construction and management of a geometric path and implementation of the
     * above interfaces with little added interpretation. If it is useful to
     * manipulate the interiors of closed geometric shapes beyond simple hit testing
     * then the {@link Area} class provides additional capabilities specifically
     * targeted at closed figures. While both classes nominally implement the
     * {@code Shape} interface, they differ in purpose and together they provide two
     * useful views of a geometric shape where {@code Path2D} deals primarily with a
     * trajectory formed by path segments and {@code Area} deals more with
     * interpretation and manipulation of enclosed regions of 2D geometric space.
     * <p>
     * The {@link PathIterator} interface has more detailed descriptions of the
     * types of segments that make up a path and the winding rules that control how
     * to determine which regions are inside or outside the path.
     *
     * @author Jim Graham
     * @since 1.6
     */
    abstract class Path2D implements java.awt.Shape, java.lang.Cloneable {
        abstract getBounds2D(): any;
        /**
         * An even-odd winding rule for determining the interior of a path.
         *
         * @see PathIterator#WIND_EVEN_ODD
         * @since 1.6
         */
        static WIND_EVEN_ODD: number;
        static WIND_EVEN_ODD_$LI$(): number;
        /**
         * A non-zero winding rule for determining the interior of a path.
         *
         * @see PathIterator#WIND_NON_ZERO
         * @since 1.6
         */
        static WIND_NON_ZERO: number;
        static WIND_NON_ZERO_$LI$(): number;
        static SEG_MOVETO: number;
        static SEG_MOVETO_$LI$(): number;
        static SEG_LINETO: number;
        static SEG_LINETO_$LI$(): number;
        static SEG_QUADTO: number;
        static SEG_QUADTO_$LI$(): number;
        static SEG_CUBICTO: number;
        static SEG_CUBICTO_$LI$(): number;
        static SEG_CLOSE: number;
        static SEG_CLOSE_$LI$(): number;
        pointTypes: number[];
        numTypes: number;
        numCoords: number;
        windingRule: number;
        static INIT_SIZE: number;
        static EXPAND_MAX: number;
        /**
         * Constructs a new {@code Path2D} object from the given specified initial
         * values. This method is only intended for internal use and should not be
         * made public if the other constructors for this class are ever exposed.
         *
         * @param rule
         * the winding rule
         * @param initialTypes
         * the size to make the initial array to store the path segment
         * types
         * @since 1.6
         */
        constructor(rule?: any, initialTypes?: any);
        abstract cloneCoordsFloat(at: java.awt.geom.AffineTransform): number[];
        abstract cloneCoordsDouble(at: java.awt.geom.AffineTransform): number[];
        append$float$float(x: number, y: number): void;
        append$double$double(x: number, y: number): void;
        abstract getPoint(coordindex: number): java.awt.geom.Point2D;
        abstract needRoom(needMove: boolean, newCoords: number): any;
        abstract pointCrossings(px: number, py: number): number;
        abstract rectCrossings(rxmin: number, rymin: number, rxmax: number, rymax: number): number;
        /**
         * Adds a point to the path by moving to the specified coordinates
         * specified in float precision.
         * <p>
         * This method provides a single precision variant of the double
         * precision {@code moveTo()} method on the base {@code Path2D} class.
         *
         * @param x
         * the specified X coordinate
         * @param y
         * the specified Y coordinate
         * @see Path2D#moveTo
         * @since 1.6
         */
        moveTo(x?: any, y?: any): any;
        /**
         * Adds a point to the path by moving to the specified coordinates specified
         * in double precision.
         *
         * @param x
         * the specified X coordinate
         * @param y
         * the specified Y coordinate
         * @since 1.6
         */
        moveTo$double$double(x: number, y: number): void;
        /**
         * Adds a point to the path by drawing a straight line from the current
         * coordinates to the new specified coordinates specified in float
         * precision.
         * <p>
         * This method provides a single precision variant of the double
         * precision {@code lineTo()} method on the base {@code Path2D} class.
         *
         * @param x
         * the specified X coordinate
         * @param y
         * the specified Y coordinate
         * @see Path2D#lineTo
         * @since 1.6
         */
        lineTo(x?: any, y?: any): any;
        /**
         * Adds a point to the path by drawing a straight line from the current
         * coordinates to the new specified coordinates specified in double
         * precision.
         *
         * @param x
         * the specified X coordinate
         * @param y
         * the specified Y coordinate
         * @since 1.6
         */
        lineTo$double$double(x: number, y: number): void;
        /**
         * Adds a curved segment, defined by two new points, to the path by
         * drawing a Quadratic curve that intersects both the current
         * coordinates and the specified coordinates {@code (x2,y2)}, using the
         * specified point {@code (x1,y1)} as a quadratic parametric control
         * point. All coordinates are specified in float precision.
         * <p>
         * This method provides a single precision variant of the double
         * precision {@code quadTo()} method on the base {@code Path2D} class.
         *
         * @param x1
         * the X coordinate of the quadratic control point
         * @param y1
         * the Y coordinate of the quadratic control point
         * @param x2
         * the X coordinate of the final end point
         * @param y2
         * the Y coordinate of the final end point
         * @see Path2D#quadTo
         * @since 1.6
         */
        quadTo(x1?: any, y1?: any, x2?: any, y2?: any): any;
        /**
         * Adds a curved segment, defined by two new points, to the path by drawing
         * a Quadratic curve that intersects both the current coordinates and the
         * specified coordinates {@code (x2,y2)}, using the specified point
         * {@code (x1,y1)} as a quadratic parametric control point. All coordinates
         * are specified in double precision.
         *
         * @param x1
         * the X coordinate of the quadratic control point
         * @param y1
         * the Y coordinate of the quadratic control point
         * @param x2
         * the X coordinate of the final end point
         * @param y2
         * the Y coordinate of the final end point
         * @since 1.6
         */
        quadTo$double$double$double$double(x1: number, y1: number, x2: number, y2: number): void;
        /**
         * Adds a curved segment, defined by three new points, to the path by
         * drawing a B&eacute;zier curve that intersects both the current
         * coordinates and the specified coordinates {@code (x3,y3)}, using the
         * specified points {@code (x1,y1)} and {@code (x2,y2)} as B&eacute;zier
         * control points. All coordinates are specified in float precision.
         * <p>
         * This method provides a single precision variant of the double
         * precision {@code curveTo()} method on the base {@code Path2D} class.
         *
         * @param x1
         * the X coordinate of the first B&eacute;zier control point
         * @param y1
         * the Y coordinate of the first B&eacute;zier control point
         * @param x2
         * the X coordinate of the second B&eacute;zier control point
         * @param y2
         * the Y coordinate of the second B&eacute;zier control point
         * @param x3
         * the X coordinate of the final end point
         * @param y3
         * the Y coordinate of the final end point
         * @see Path2D#curveTo
         * @since 1.6
         */
        curveTo(x1?: any, y1?: any, x2?: any, y2?: any, x3?: any, y3?: any): any;
        /**
         * Adds a curved segment, defined by three new points, to the path by
         * drawing a B&eacute;zier curve that intersects both the current
         * coordinates and the specified coordinates {@code (x3,y3)}, using the
         * specified points {@code (x1,y1)} and {@code (x2,y2)} as B&eacute;zier
         * control points. All coordinates are specified in double precision.
         *
         * @param x1
         * the X coordinate of the first B&eacute;zier control point
         * @param y1
         * the Y coordinate of the first B&eacute;zier control point
         * @param x2
         * the X coordinate of the second B&eacute;zier control point
         * @param y2
         * the Y coordinate of the second B&eacute;zier control point
         * @param x3
         * the X coordinate of the final end point
         * @param y3
         * the Y coordinate of the final end point
         * @since 1.6
         */
        curveTo$double$double$double$double$double$double(x1: number, y1: number, x2: number, y2: number, x3: number, y3: number): void;
        /**
         * Closes the current subpath by drawing a straight line back to the
         * coordinates of the last {@code moveTo}. If the path is already closed
         * then this method has no effect.
         *
         * @since 1.6
         */
        closePath(): void;
        /**
         * Appends the geometry of the specified {@code Shape} object to the path,
         * possibly connecting the new geometry to the existing path segments with a
         * line segment. If the {@code connect} parameter is {@code true} and the
         * path is not empty then any initial {@code moveTo} in the geometry of the
         * appended {@code Shape} is turned into a {@code lineTo} segment. If the
         * destination coordinates of such a connecting {@code lineTo} segment match
         * the ending coordinates of a currently open subpath then the segment is
         * omitted as superfluous. The winding rule of the specified {@code Shape}
         * is ignored and the appended geometry is governed by the winding rule
         * specified for this path.
         *
         * @param s
         * the {@code Shape} whose geometry is appended to this path
         * @param connect
         * a boolean to control whether or not to turn an initial
         * {@code moveTo} segment into a {@code lineTo} segment to
         * connect the new geometry to the existing path
         * @since 1.6
         */
        append(s?: any, connect?: any): any;
        /**
         * Appends the geometry of the specified {@link PathIterator} object to the
         * path, possibly connecting the new geometry to the existing path segments
         * with a line segment. If the {@code connect} parameter is {@code true} and
         * the path is not empty then any initial {@code moveTo} in the geometry of
         * the appended {@code Shape} is turned into a {@code lineTo} segment. If
         * the destination coordinates of such a connecting {@code lineTo} segment
         * match the ending coordinates of a currently open subpath then the segment
         * is omitted as superfluous. The winding rule of the specified
         * {@code Shape} is ignored and the appended geometry is governed by the
         * winding rule specified for this path.
         *
         * @param pi
         * the {@code PathIterator} whose geometry is appended to this
         * path
         * @param connect
         * a boolean to control whether or not to turn an initial
         * {@code moveTo} segment into a {@code lineTo} segment to
         * connect the new geometry to the existing path
         * @since 1.6
         */
        append$java_awt_geom_PathIterator$boolean(pi: java.awt.geom.PathIterator, connect: boolean): void;
        /**
         * Returns the fill style winding rule.
         *
         * @return an integer representing the current winding rule.
         * @see #WIND_EVEN_ODD
         * @see #WIND_NON_ZERO
         * @see #setWindingRule
         * @since 1.6
         */
        getWindingRule(): number;
        /**
         * Sets the winding rule for this path to the specified value.
         *
         * @param rule
         * an integer representing the specified winding rule
         * @exception IllegalArgumentException
         * if {@code rule} is not either {@link #WIND_EVEN_ODD} or
         * {@link #WIND_NON_ZERO}
         * @see #getWindingRule
         * @since 1.6
         */
        setWindingRule(rule: number): void;
        /**
         * Returns the coordinates most recently added to the end of the path as a
         * {@link Point2D} object.
         *
         * @return a {@code Point2D} object containing the ending coordinates of the
         * path or {@code null} if there are no points in the path.
         * @since 1.6
         */
        getCurrentPoint(): java.awt.geom.Point2D;
        /**
         * Resets the path to empty. The append position is set back to the
         * beginning of the path and all coordinates and point types are forgotten.
         *
         * @since 1.6
         */
        reset(): void;
        /**
         * Transforms the geometry of this path using the specified
         * {@link AffineTransform}. The geometry is transformed in place, which
         * permanently changes the boundary defined by this object.
         *
         * @param at
         * the {@code AffineTransform} used to transform the area
         * @since 1.6
         */
        abstract transform(at: java.awt.geom.AffineTransform): any;
        /**
         * Returns a new {@code Shape} representing a transformed version of this
         * {@code Path2D}. Note that the exact type and coordinate precision of the
         * return value is not specified for this method. The method will return a
         * Shape that contains no less precision for the transformed geometry than
         * this {@code Path2D} currently maintains, but it may contain no more
         * precision either. If the tradeoff of precision vs. storage size in the
         * result is important then the convenience constructors in the
         * {@link Path2D.Float#Path2D.Float(Shape, AffineTransform) Path2D.Float}
         * and {@link Path2D.Double#Path2D.Double(Shape, AffineTransform)
         * Path2D.Double} subclasses should be used to make the choice explicit.
         *
         * @param at
         * the {@code AffineTransform} used to transform a new
         * {@code Shape}.
         * @return a new {@code Shape}, transformed with the specified
         * {@code AffineTransform}.
         * @since 1.6
         */
        createTransformedShape(at: java.awt.geom.AffineTransform): java.awt.Shape;
        /**
         * {@inheritDoc}
         *
         * @since 1.6
         */
        getBounds(): java.awt.Rectangle;
        /**
         * Tests if the specified coordinates are inside the closed boundary of the
         * specified {@link PathIterator}.
         * <p>
         * This method provides a basic facility for implementors of the
         * {@link Shape} interface to implement support for the
         * {@link Shape#contains(double, double)} method.
         *
         * @param pi
         * the specified {@code PathIterator}
         * @param x
         * the specified X coordinate
         * @param y
         * the specified Y coordinate
         * @return {@code true} if the specified coordinates are inside the
         * specified {@code PathIterator}; {@code false} otherwise
         * @since 1.6
         */
        static contains$java_awt_geom_PathIterator$double$double(pi: java.awt.geom.PathIterator, x: number, y: number): boolean;
        /**
         * Tests if the specified {@link Point2D} is inside the closed boundary of
         * the specified {@link PathIterator}.
         * <p>
         * This method provides a basic facility for implementors of the
         * {@link Shape} interface to implement support for the
         * {@link Shape#contains(Point2D)} method.
         *
         * @param pi
         * the specified {@code PathIterator}
         * @param p
         * the specified {@code Point2D}
         * @return {@code true} if the specified coordinates are inside the
         * specified {@code PathIterator}; {@code false} otherwise
         * @since 1.6
         */
        static contains$java_awt_geom_PathIterator$java_awt_geom_Point2D(pi: java.awt.geom.PathIterator, p: java.awt.geom.Point2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.6
         */
        contains$double$double(x: number, y: number): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.6
         */
        contains$java_awt_geom_Point2D(p: java.awt.geom.Point2D): boolean;
        /**
         * Tests if the specified rectangular area is entirely inside the closed
         * boundary of the specified {@link PathIterator}.
         * <p>
         * This method provides a basic facility for implementors of the
         * {@link Shape} interface to implement support for the
         * {@link Shape#contains(double, double, double, double)} method.
         * <p>
         * This method object may conservatively return false in cases where the
         * specified rectangular area intersects a segment of the path, but that
         * segment does not represent a boundary between the interior and exterior
         * of the path. Such segments could lie entirely within the interior of the
         * path if they are part of a path with a {@link #WIND_NON_ZERO} winding
         * rule or if the segments are retraced in the reverse direction such that
         * the two sets of segments cancel each other out without any exterior area
         * falling between them. To determine whether segments represent true
         * boundaries of the interior of the path would require extensive
         * calculations involving all of the segments of the path and the winding
         * rule and are thus beyond the scope of this implementation.
         *
         * @param pi
         * the specified {@code PathIterator}
         * @param x
         * the specified X coordinate
         * @param y
         * the specified Y coordinate
         * @param w
         * the width of the specified rectangular area
         * @param h
         * the height of the specified rectangular area
         * @return {@code true} if the specified {@code PathIterator} contains the
         * specified rectangular area; {@code false} otherwise.
         * @since 1.6
         */
        static contains(pi?: any, x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Tests if the specified {@link Rectangle2D} is entirely inside the closed
         * boundary of the specified {@link PathIterator}.
         * <p>
         * This method provides a basic facility for implementors of the
         * {@link Shape} interface to implement support for the
         * {@link Shape#contains(Rectangle2D)} method.
         * <p>
         * This method object may conservatively return false in cases where the
         * specified rectangular area intersects a segment of the path, but that
         * segment does not represent a boundary between the interior and exterior
         * of the path. Such segments could lie entirely within the interior of the
         * path if they are part of a path with a {@link #WIND_NON_ZERO} winding
         * rule or if the segments are retraced in the reverse direction such that
         * the two sets of segments cancel each other out without any exterior area
         * falling between them. To determine whether segments represent true
         * boundaries of the interior of the path would require extensive
         * calculations involving all of the segments of the path and the winding
         * rule and are thus beyond the scope of this implementation.
         *
         * @param pi
         * the specified {@code PathIterator}
         * @param r
         * a specified {@code Rectangle2D}
         * @return {@code true} if the specified {@code PathIterator} contains the
         * specified {@code Rectangle2D}; {@code false} otherwise.
         * @since 1.6
         */
        static contains$java_awt_geom_PathIterator$java_awt_geom_Rectangle2D(pi: java.awt.geom.PathIterator, r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         * <p>
         * This method object may conservatively return false in cases where the
         * specified rectangular area intersects a segment of the path, but that
         * segment does not represent a boundary between the interior and exterior
         * of the path. Such segments could lie entirely within the interior of the
         * path if they are part of a path with a {@link #WIND_NON_ZERO} winding
         * rule or if the segments are retraced in the reverse direction such that
         * the two sets of segments cancel each other out without any exterior area
         * falling between them. To determine whether segments represent true
         * boundaries of the interior of the path would require extensive
         * calculations involving all of the segments of the path and the winding
         * rule and are thus beyond the scope of this implementation.
         *
         * @since 1.6
         */
        contains(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         * <p>
         * This method object may conservatively return false in cases where the
         * specified rectangular area intersects a segment of the path, but that
         * segment does not represent a boundary between the interior and exterior
         * of the path. Such segments could lie entirely within the interior of the
         * path if they are part of a path with a {@link #WIND_NON_ZERO} winding
         * rule or if the segments are retraced in the reverse direction such that
         * the two sets of segments cancel each other out without any exterior area
         * falling between them. To determine whether segments represent true
         * boundaries of the interior of the path would require extensive
         * calculations involving all of the segments of the path and the winding
         * rule and are thus beyond the scope of this implementation.
         *
         * @since 1.6
         */
        contains$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * Tests if the interior of the specified {@link PathIterator} intersects
         * the interior of a specified set of rectangular coordinates.
         * <p>
         * This method provides a basic facility for implementors of the
         * {@link Shape} interface to implement support for the
         * {@link Shape#intersects(double, double, double, double)} method.
         * <p>
         * This method object may conservatively return true in cases where the
         * specified rectangular area intersects a segment of the path, but that
         * segment does not represent a boundary between the interior and exterior
         * of the path. Such a case may occur if some set of segments of the path
         * are retraced in the reverse direction such that the two sets of segments
         * cancel each other out without any interior area between them. To
         * determine whether segments represent true boundaries of the interior of
         * the path would require extensive calculations involving all of the
         * segments of the path and the winding rule and are thus beyond the scope
         * of this implementation.
         *
         * @param pi
         * the specified {@code PathIterator}
         * @param x
         * the specified X coordinate
         * @param y
         * the specified Y coordinate
         * @param w
         * the width of the specified rectangular coordinates
         * @param h
         * the height of the specified rectangular coordinates
         * @return {@code true} if the specified {@code PathIterator} and the
         * interior of the specified set of rectangular coordinates
         * intersect each other; {@code false} otherwise.
         * @since 1.6
         */
        static intersects(pi?: any, x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Tests if the interior of the specified {@link PathIterator} intersects
         * the interior of a specified {@link Rectangle2D}.
         * <p>
         * This method provides a basic facility for implementors of the
         * {@link Shape} interface to implement support for the
         * {@link Shape#intersects(Rectangle2D)} method.
         * <p>
         * This method object may conservatively return true in cases where the
         * specified rectangular area intersects a segment of the path, but that
         * segment does not represent a boundary between the interior and exterior
         * of the path. Such a case may occur if some set of segments of the path
         * are retraced in the reverse direction such that the two sets of segments
         * cancel each other out without any interior area between them. To
         * determine whether segments represent true boundaries of the interior of
         * the path would require extensive calculations involving all of the
         * segments of the path and the winding rule and are thus beyond the scope
         * of this implementation.
         *
         * @param pi
         * the specified {@code PathIterator}
         * @param r
         * the specified {@code Rectangle2D}
         * @return {@code true} if the specified {@code PathIterator} and the
         * interior of the specified {@code Rectangle2D} intersect each
         * other; {@code false} otherwise.
         * @since 1.6
         */
        static intersects$java_awt_geom_PathIterator$java_awt_geom_Rectangle2D(pi: java.awt.geom.PathIterator, r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         * <p>
         * This method object may conservatively return true in cases where the
         * specified rectangular area intersects a segment of the path, but that
         * segment does not represent a boundary between the interior and exterior
         * of the path. Such a case may occur if some set of segments of the path
         * are retraced in the reverse direction such that the two sets of segments
         * cancel each other out without any interior area between them. To
         * determine whether segments represent true boundaries of the interior of
         * the path would require extensive calculations involving all of the
         * segments of the path and the winding rule and are thus beyond the scope
         * of this implementation.
         *
         * @since 1.6
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         * <p>
         * This method object may conservatively return true in cases where the
         * specified rectangular area intersects a segment of the path, but that
         * segment does not represent a boundary between the interior and exterior
         * of the path. Such a case may occur if some set of segments of the path
         * are retraced in the reverse direction such that the two sets of segments
         * cancel each other out without any interior area between them. To
         * determine whether segments represent true boundaries of the interior of
         * the path would require extensive calculations involving all of the
         * segments of the path and the winding rule and are thus beyond the scope
         * of this implementation.
         *
         * @since 1.6
         */
        intersects$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         * <p>
         * The iterator for this class is not multi-threaded safe, which means that
         * this {@code Path2D} class does not guarantee that modifications to the
         * geometry of this {@code Path2D} object do not affect any iterations of
         * that geometry that are already in process.
         *
         * @since 1.6
         */
        getPathIterator(at?: any, flatness?: any): any;
        /**
         * Creates a new object of the same class as this object.
         *
         * @return a clone of this instance.
         * @exception OutOfMemoryError
         * if there is not enough memory.
         * @see java.lang.Cloneable
         * @since 1.6
         */
        abstract clone(): any;
    }
    namespace Path2D {
        abstract class Iterator implements java.awt.geom.PathIterator {
            abstract currentSegment(coords: any): any;
            abstract currentSegment(coords: any): any;
            typeIdx: number;
            pointIdx: number;
            path: java.awt.geom.Path2D;
            static curvecoords: number[];
            static curvecoords_$LI$(): number[];
            constructor(path: java.awt.geom.Path2D);
            getWindingRule(): number;
            isDone(): boolean;
            next(doNext?: any): any;
            next$(): void;
        }
        /**
         * The {@code Float} class defines a geometric path with coordinates stored
         * in single precision floating point.
         *
         * @since 1.6
         */
        class Float extends java.awt.geom.Path2D implements java.io.Serializable {
            floatCoords: number[];
            /**
             * Constructs a new single precision {@code Path2D} object from an
             * arbitrary {@link Shape} object, transformed by an
             * {@link AffineTransform} object. All of the initial geometry and the
             * winding rule for this path are taken from the specified {@code Shape}
             * object and transformed by the specified {@code AffineTransform}
             * object.
             *
             * @param s
             * the specified {@code Shape} object
             * @param at
             * the specified {@code AffineTransform} object
             * @since 1.6
             */
            constructor(s?: any, at?: any);
            cloneCoordsFloat(at: java.awt.geom.AffineTransform): number[];
            cloneCoordsDouble(at: java.awt.geom.AffineTransform): number[];
            append$float$float(x: number, y: number): void;
            append$double$double(x: number, y: number): void;
            getPoint(coordindex: number): java.awt.geom.Point2D;
            needRoom(needMove: boolean, newCoords: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            moveTo$double$double(x: number, y: number): void;
            /**
             * Adds a point to the path by moving to the specified coordinates
             * specified in float precision.
             * <p>
             * This method provides a single precision variant of the double
             * precision {@code moveTo()} method on the base {@code Path2D} class.
             *
             * @param x
             * the specified X coordinate
             * @param y
             * the specified Y coordinate
             * @see Path2D#moveTo
             * @since 1.6
             */
            moveTo(x?: any, y?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            lineTo$double$double(x: number, y: number): void;
            /**
             * Adds a point to the path by drawing a straight line from the current
             * coordinates to the new specified coordinates specified in float
             * precision.
             * <p>
             * This method provides a single precision variant of the double
             * precision {@code lineTo()} method on the base {@code Path2D} class.
             *
             * @param x
             * the specified X coordinate
             * @param y
             * the specified Y coordinate
             * @see Path2D#lineTo
             * @since 1.6
             */
            lineTo(x?: any, y?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            quadTo$double$double$double$double(x1: number, y1: number, x2: number, y2: number): void;
            /**
             * Adds a curved segment, defined by two new points, to the path by
             * drawing a Quadratic curve that intersects both the current
             * coordinates and the specified coordinates {@code (x2,y2)}, using the
             * specified point {@code (x1,y1)} as a quadratic parametric control
             * point. All coordinates are specified in float precision.
             * <p>
             * This method provides a single precision variant of the double
             * precision {@code quadTo()} method on the base {@code Path2D} class.
             *
             * @param x1
             * the X coordinate of the quadratic control point
             * @param y1
             * the Y coordinate of the quadratic control point
             * @param x2
             * the X coordinate of the final end point
             * @param y2
             * the Y coordinate of the final end point
             * @see Path2D#quadTo
             * @since 1.6
             */
            quadTo(x1?: any, y1?: any, x2?: any, y2?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            curveTo$double$double$double$double$double$double(x1: number, y1: number, x2: number, y2: number, x3: number, y3: number): void;
            /**
             * Adds a curved segment, defined by three new points, to the path by
             * drawing a B&eacute;zier curve that intersects both the current
             * coordinates and the specified coordinates {@code (x3,y3)}, using the
             * specified points {@code (x1,y1)} and {@code (x2,y2)} as B&eacute;zier
             * control points. All coordinates are specified in float precision.
             * <p>
             * This method provides a single precision variant of the double
             * precision {@code curveTo()} method on the base {@code Path2D} class.
             *
             * @param x1
             * the X coordinate of the first B&eacute;zier control point
             * @param y1
             * the Y coordinate of the first B&eacute;zier control point
             * @param x2
             * the X coordinate of the second B&eacute;zier control point
             * @param y2
             * the Y coordinate of the second B&eacute;zier control point
             * @param x3
             * the X coordinate of the final end point
             * @param y3
             * the Y coordinate of the final end point
             * @see Path2D#curveTo
             * @since 1.6
             */
            curveTo(x1?: any, y1?: any, x2?: any, y2?: any, x3?: any, y3?: any): any;
            pointCrossings(px: number, py: number): number;
            rectCrossings(rxmin: number, rymin: number, rxmax: number, rymax: number): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            append(pi?: any, connect?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            transform(at: java.awt.geom.AffineTransform): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            /**
             * {@inheritDoc}
             * <p>
             * The iterator for this class is not multi-threaded safe, which means
             * that the {@code Path2D} class does not guarantee that modifications
             * to the geometry of this {@code Path2D} object do not affect any
             * iterations of that geometry that are already in process.
             *
             * @since 1.6
             */
            getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
            /**
             * Creates a new object of the same class as this object.
             *
             * @return a clone of this instance.
             * @exception OutOfMemoryError
             * if there is not enough memory.
             * @see java.lang.Cloneable
             * @since 1.6
             */
            clone(): any;
            static serialVersionUID: number;
        }
        namespace Float {
            class CopyIterator extends java.awt.geom.Path2D.Iterator {
                floatCoords: number[];
                constructor(p2df: java.awt.geom.Path2D.Float);
                currentSegment(coords?: any): any;
                currentSegment$double_A(coords: number[]): number;
            }
            class TxIterator extends java.awt.geom.Path2D.Iterator {
                floatCoords: number[];
                affine: java.awt.geom.AffineTransform;
                constructor(p2df: java.awt.geom.Path2D.Float, at: java.awt.geom.AffineTransform);
                currentSegment(coords?: any): any;
                currentSegment$double_A(coords: number[]): number;
            }
        }
        /**
         * The {@code Double} class defines a geometric path with coordinates stored
         * in double precision floating point.
         *
         * @since 1.6
         */
        class Double extends java.awt.geom.Path2D implements java.io.Serializable {
            doubleCoords: number[];
            /**
             * Constructs a new double precision {@code Path2D} object from an
             * arbitrary {@link Shape} object, transformed by an
             * {@link AffineTransform} object. All of the initial geometry and the
             * winding rule for this path are taken from the specified {@code Shape}
             * object and transformed by the specified {@code AffineTransform}
             * object.
             *
             * @param s
             * the specified {@code Shape} object
             * @param at
             * the specified {@code AffineTransform} object
             * @since 1.6
             */
            constructor(s?: any, at?: any);
            cloneCoordsFloat(at: java.awt.geom.AffineTransform): number[];
            cloneCoordsDouble(at: java.awt.geom.AffineTransform): number[];
            append$float$float(x: number, y: number): void;
            append$double$double(x: number, y: number): void;
            getPoint(coordindex: number): java.awt.geom.Point2D;
            needRoom(needMove: boolean, newCoords: number): void;
            /**
             * Adds a point to the path by moving to the specified coordinates
             * specified in float precision.
             * <p>
             * This method provides a single precision variant of the double
             * precision {@code moveTo()} method on the base {@code Path2D} class.
             *
             * @param x
             * the specified X coordinate
             * @param y
             * the specified Y coordinate
             * @see Path2D#moveTo
             * @since 1.6
             */
            moveTo(x?: any, y?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            moveTo$double$double(x: number, y: number): void;
            /**
             * Adds a point to the path by drawing a straight line from the current
             * coordinates to the new specified coordinates specified in float
             * precision.
             * <p>
             * This method provides a single precision variant of the double
             * precision {@code lineTo()} method on the base {@code Path2D} class.
             *
             * @param x
             * the specified X coordinate
             * @param y
             * the specified Y coordinate
             * @see Path2D#lineTo
             * @since 1.6
             */
            lineTo(x?: any, y?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            lineTo$double$double(x: number, y: number): void;
            /**
             * Adds a curved segment, defined by two new points, to the path by
             * drawing a Quadratic curve that intersects both the current
             * coordinates and the specified coordinates {@code (x2,y2)}, using the
             * specified point {@code (x1,y1)} as a quadratic parametric control
             * point. All coordinates are specified in float precision.
             * <p>
             * This method provides a single precision variant of the double
             * precision {@code quadTo()} method on the base {@code Path2D} class.
             *
             * @param x1
             * the X coordinate of the quadratic control point
             * @param y1
             * the Y coordinate of the quadratic control point
             * @param x2
             * the X coordinate of the final end point
             * @param y2
             * the Y coordinate of the final end point
             * @see Path2D#quadTo
             * @since 1.6
             */
            quadTo(x1?: any, y1?: any, x2?: any, y2?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            quadTo$double$double$double$double(x1: number, y1: number, x2: number, y2: number): void;
            /**
             * Adds a curved segment, defined by three new points, to the path by
             * drawing a B&eacute;zier curve that intersects both the current
             * coordinates and the specified coordinates {@code (x3,y3)}, using the
             * specified points {@code (x1,y1)} and {@code (x2,y2)} as B&eacute;zier
             * control points. All coordinates are specified in float precision.
             * <p>
             * This method provides a single precision variant of the double
             * precision {@code curveTo()} method on the base {@code Path2D} class.
             *
             * @param x1
             * the X coordinate of the first B&eacute;zier control point
             * @param y1
             * the Y coordinate of the first B&eacute;zier control point
             * @param x2
             * the X coordinate of the second B&eacute;zier control point
             * @param y2
             * the Y coordinate of the second B&eacute;zier control point
             * @param x3
             * the X coordinate of the final end point
             * @param y3
             * the Y coordinate of the final end point
             * @see Path2D#curveTo
             * @since 1.6
             */
            curveTo(x1?: any, y1?: any, x2?: any, y2?: any, x3?: any, y3?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            curveTo$double$double$double$double$double$double(x1: number, y1: number, x2: number, y2: number, x3: number, y3: number): void;
            pointCrossings(px: number, py: number): number;
            rectCrossings(rxmin: number, rymin: number, rxmax: number, rymax: number): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            append(pi?: any, connect?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            transform(at: java.awt.geom.AffineTransform): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.6
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            /**
             * {@inheritDoc}
             * <p>
             * The iterator for this class is not multi-threaded safe, which means
             * that the {@code Path2D} class does not guarantee that modifications
             * to the geometry of this {@code Path2D} object do not affect any
             * iterations of that geometry that are already in process.
             *
             * @param at
             * an {@code AffineTransform}
             * @return a new {@code PathIterator} that iterates along the boundary
             * of this {@code Shape} and provides access to the geometry of
             * this {@code Shape}'s outline
             * @since 1.6
             */
            getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
            /**
             * Creates a new object of the same class as this object.
             *
             * @return a clone of this instance.
             * @exception OutOfMemoryError
             * if there is not enough memory.
             * @see java.lang.Cloneable
             * @since 1.6
             */
            clone(): any;
            static serialVersionUID: number;
        }
        namespace Double {
            class CopyIterator extends java.awt.geom.Path2D.Iterator {
                doubleCoords: number[];
                constructor(p2dd: java.awt.geom.Path2D.Double);
                currentSegment(coords?: any): any;
                currentSegment$double_A(coords: number[]): number;
            }
            class TxIterator extends java.awt.geom.Path2D.Iterator {
                doubleCoords: number[];
                affine: java.awt.geom.AffineTransform;
                constructor(p2dd: java.awt.geom.Path2D.Double, at: java.awt.geom.AffineTransform);
                currentSegment(coords?: any): any;
                currentSegment$double_A(coords: number[]): number;
            }
        }
    }
}
declare namespace java.awt {
    /**
     * A point representing a location in {@code (x,y)} coordinate space,
     * specified in integer precision.
     *
     * @author      Sami Shaio
     * @since       1.0
     */
    class Point extends java.awt.geom.Point2D implements java.io.Serializable {
        /**
         * The X coordinate of this <code>Point</code>.
         * If no X coordinate is set it will default to 0.
         *
         * @serial
         * @see #getLocation()
         * @see #move(int, int)
         * @since 1.0
         */
        x: number;
        /**
         * The Y coordinate of this <code>Point</code>.
         * If no Y coordinate is set it will default to 0.
         *
         * @serial
         * @see #getLocation()
         * @see #move(int, int)
         * @since 1.0
         */
        y: number;
        static serialVersionUID: number;
        /**
         * Constructs and initializes a point at the specified
         * {@code (x,y)} location in the coordinate space.
         * @param x the X coordinate of the newly constructed <code>Point</code>
         * @param y the Y coordinate of the newly constructed <code>Point</code>
         * @since 1.0
         */
        constructor(x?: any, y?: any);
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        getX(): number;
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        getY(): number;
        /**
         * Returns the location of this point.
         * This method is included for completeness, to parallel the
         * <code>getLocation</code> method of <code>Component</code>.
         * @return      a copy of this point, at the same location
         * @see         java.awt.Component#getLocation
         * @see         java.awt.Point#setLocation(java.awt.Point)
         * @see         java.awt.Point#setLocation(int, int)
         * @since       1.1
         */
        getLocation(): Point;
        /**
         * Sets the location of the point to the specified location.
         * This method is included for completeness, to parallel the
         * <code>setLocation</code> method of <code>Component</code>.
         * @param       p  a point, the new location for this point
         * @see         java.awt.Component#setLocation(java.awt.Point)
         * @see         java.awt.Point#getLocation
         * @since       1.1
         */
        setLocation$java_awt_Point(p: Point): void;
        /**
         * Changes the point to have the specified location.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>setLocation</code> method of <code>Component</code>.
         * Its behavior is identical with <code>move(int,&nbsp;int)</code>.
         * @param       x the X coordinate of the new location
         * @param       y the Y coordinate of the new location
         * @see         java.awt.Component#setLocation(int, int)
         * @see         java.awt.Point#getLocation
         * @see         java.awt.Point#move(int, int)
         * @since       1.1
         */
        setLocation(x?: any, y?: any): any;
        /**
         * Sets the location of this point to the specified double coordinates.
         * The double values will be rounded to integer values.
         * Any number smaller than <code>Integer.MIN_VALUE</code>
         * will be reset to <code>MIN_VALUE</code>, and any number
         * larger than <code>Integer.MAX_VALUE</code> will be
         * reset to <code>MAX_VALUE</code>.
         *
         * @param x the X coordinate of the new location
         * @param y the Y coordinate of the new location
         * @see #getLocation
         */
        setLocation$double$double(x: number, y: number): void;
        /**
         * Moves this point to the specified location in the
         * {@code (x,y)} coordinate plane. This method
         * is identical with <code>setLocation(int,&nbsp;int)</code>.
         * @param       x the X coordinate of the new location
         * @param       y the Y coordinate of the new location
         * @see         java.awt.Component#setLocation(int, int)
         */
        move(x: number, y: number): void;
        /**
         * Translates this point, at location {@code (x,y)},
         * by {@code dx} along the {@code x} axis and {@code dy}
         * along the {@code y} axis so that it now represents the point
         * {@code (x+dx,y+dy)}.
         *
         * @param       dx   the distance to move this point
         * along the X axis
         * @param       dy    the distance to move this point
         * along the Y axis
         */
        translate(dx: number, dy: number): void;
        /**
         * Determines whether or not two points are equal. Two instances of
         * <code>Point2D</code> are equal if the values of their
         * <code>x</code> and <code>y</code> member fields, representing
         * their position in the coordinate space, are the same.
         * @param obj an object to be compared with this <code>Point2D</code>
         * @return <code>true</code> if the object to be compared is
         * an instance of <code>Point2D</code> and has
         * the same values; <code>false</code> otherwise.
         */
        equals(obj: any): boolean;
        /**
         * Returns a string representation of this point and its location
         * in the {@code (x,y)} coordinate space. This method is
         * intended to be used only for debugging purposes, and the content
         * and format of the returned string may vary between implementations.
         * The returned string may be empty but may not be <code>null</code>.
         *
         * @return  a string representation of this point
         */
        toString(): string;
    }
}
declare namespace java.awt.geom {
    /**
     * <CODE>Arc2D</CODE> is the abstract superclass for all objects that
     * store a 2D arc defined by a framing rectangle,
     * start angle, angular extent (length of the arc), and a closure type
     * (<CODE>OPEN</CODE>, <CODE>CHORD</CODE>, or <CODE>PIE</CODE>).
     * <p>
     * <a name="inscribes">
     * The arc is a partial section of a full ellipse which
     * inscribes the framing rectangle of its parent {@link RectangularShape}.
     * </a>
     * <a name="angles">
     * The angles are specified relative to the non-square
     * framing rectangle such that 45 degrees always falls on the line from
     * the center of the ellipse to the upper right corner of the framing
     * rectangle.
     * As a result, if the framing rectangle is noticeably longer along one
     * axis than the other, the angles to the start and end of the arc segment
     * will be skewed farther along the longer axis of the frame.
     * </a>
     * <p>
     * The actual storage representation of the coordinates is left to
     * the subclass.
     *
     * @author      Jim Graham
     * @since 1.2
     */
    abstract class Arc2D extends java.awt.geom.RectangularShape {
        /**
         * The closure type for an open arc with no path segments
         * connecting the two ends of the arc segment.
         * @since 1.2
         */
        static OPEN: number;
        /**
         * The closure type for an arc closed by drawing a straight
         * line segment from the start of the arc segment to the end of the
         * arc segment.
         * @since 1.2
         */
        static CHORD: number;
        /**
         * The closure type for an arc closed by drawing straight line
         * segments from the start of the arc segment to the center
         * of the full ellipse and from that point to the end of the arc segment.
         * @since 1.2
         */
        static PIE: number;
        private type;
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for
         * instantiation and provide a number of formats for storing
         * the information necessary to satisfy the various accessor
         * methods below.
         *
         * @param type The closure type of this arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @see java.awt.geom.Arc2D.Float
         * @see java.awt.geom.Arc2D.Double
         * @since 1.2
         */
        constructor(type?: number);
        /**
         * Returns the starting angle of the arc.
         *
         * @return A double value that represents the starting angle
         * of the arc in degrees.
         * @see #setAngleStart
         * @since 1.2
         */
        abstract getAngleStart(): number;
        /**
         * Returns the angular extent of the arc.
         *
         * @return A double value that represents the angular extent
         * of the arc in degrees.
         * @see #setAngleExtent
         * @since 1.2
         */
        abstract getAngleExtent(): number;
        /**
         * Returns the arc closure type of the arc: {@link #OPEN},
         * {@link #CHORD}, or {@link #PIE}.
         * @return One of the integer constant closure types defined
         * in this class.
         * @see #setArcType
         * @since 1.2
         */
        getArcType(): number;
        /**
         * Returns the starting point of the arc.  This point is the
         * intersection of the ray from the center defined by the
         * starting angle and the elliptical boundary of the arc.
         *
         * @return A <CODE>Point2D</CODE> object representing the
         * x,y coordinates of the starting point of the arc.
         * @since 1.2
         */
        getStartPoint(): java.awt.geom.Point2D;
        /**
         * Returns the ending point of the arc.  This point is the
         * intersection of the ray from the center defined by the
         * starting angle plus the angular extent of the arc and the
         * elliptical boundary of the arc.
         *
         * @return A <CODE>Point2D</CODE> object representing the
         * x,y coordinates  of the ending point of the arc.
         * @since 1.2
         */
        getEndPoint(): java.awt.geom.Point2D;
        /**
         * Sets the location, size, angular extents, and closure type of
         * this arc to the specified double values.
         *
         * @param x The X coordinate of the upper-left corner of the arc.
         * @param y The Y coordinate of the upper-left corner of the arc.
         * @param w The overall width of the full ellipse of which
         * this arc is a partial section.
         * @param h The overall height of the full ellipse of which
         * this arc is a partial section.
         * @param angSt The starting angle of the arc in degrees.
         * @param angExt The angular extent of the arc in degrees.
         * @param closure The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        setArc(x?: any, y?: any, w?: any, h?: any, angSt?: any, angExt?: any, closure?: any): any;
        /**
         * Sets the location, size, angular extents, and closure type of
         * this arc to the specified values.
         *
         * @param loc The <CODE>Point2D</CODE> representing the coordinates of
         * the upper-left corner of the arc.
         * @param size The <CODE>Dimension2D</CODE> representing the width
         * and height of the full ellipse of which this arc is
         * a partial section.
         * @param angSt The starting angle of the arc in degrees.
         * @param angExt The angular extent of the arc in degrees.
         * @param closure The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        setArc$java_awt_geom_Point2D$java_awt_geom_Dimension2D$double$double$int(loc: java.awt.geom.Point2D, size: java.awt.geom.Dimension2D, angSt: number, angExt: number, closure: number): void;
        /**
         * Sets the location, size, angular extents, and closure type of
         * this arc to the specified values.
         *
         * @param rect The framing rectangle that defines the
         * outer boundary of the full ellipse of which this arc is a
         * partial section.
         * @param angSt The starting angle of the arc in degrees.
         * @param angExt The angular extent of the arc in degrees.
         * @param closure The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        setArc$java_awt_geom_Rectangle2D$double$double$int(rect: java.awt.geom.Rectangle2D, angSt: number, angExt: number, closure: number): void;
        /**
         * Sets this arc to be the same as the specified arc.
         *
         * @param a The <CODE>Arc2D</CODE> to use to set the arc's values.
         * @since 1.2
         */
        setArc$java_awt_geom_Arc2D(a: Arc2D): void;
        /**
         * Sets the position, bounds, angular extents, and closure type of
         * this arc to the specified values. The arc is defined by a center
         * point and a radius rather than a framing rectangle for the full ellipse.
         *
         * @param x The X coordinate of the center of the arc.
         * @param y The Y coordinate of the center of the arc.
         * @param radius The radius of the arc.
         * @param angSt The starting angle of the arc in degrees.
         * @param angExt The angular extent of the arc in degrees.
         * @param closure The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        setArcByCenter(x: number, y: number, radius: number, angSt: number, angExt: number, closure: number): void;
        /**
         * Sets the position, bounds, and angular extents of this arc to the
         * specified value. The starting angle of the arc is tangent to the
         * line specified by points (p1, p2), the ending angle is tangent to
         * the line specified by points (p2, p3), and the arc has the
         * specified radius.
         *
         * @param p1 The first point that defines the arc. The starting
         * angle of the arc is tangent to the line specified by points (p1, p2).
         * @param p2 The second point that defines the arc. The starting
         * angle of the arc is tangent to the line specified by points (p1, p2).
         * The ending angle of the arc is tangent to the line specified by
         * points (p2, p3).
         * @param p3 The third point that defines the arc. The ending angle
         * of the arc is tangent to the line specified by points (p2, p3).
         * @param radius The radius of the arc.
         * @since 1.2
         */
        setArcByTangent(p1: java.awt.geom.Point2D, p2: java.awt.geom.Point2D, p3: java.awt.geom.Point2D, radius: number): void;
        /**
         * Sets the starting angle of this arc to the specified double
         * value.
         *
         * @param angSt The starting angle of the arc in degrees.
         * @see #getAngleStart
         * @since 1.2
         */
        setAngleStart$double(angSt: number): void;
        /**
         * Sets the angular extent of this arc to the specified double
         * value.
         *
         * @param angExt The angular extent of the arc in degrees.
         * @see #getAngleExtent
         * @since 1.2
         */
        abstract setAngleExtent(angExt: number): any;
        /**
         * Sets the starting angle of this arc to the angle that the
         * specified point defines relative to the center of this arc.
         * The angular extent of the arc will remain the same.
         *
         * @param p The <CODE>Point2D</CODE> that defines the starting angle.
         * @see #getAngleStart
         * @since 1.2
         */
        setAngleStart(p?: any): any;
        /**
         * Sets the starting angle and angular extent of this arc using two
         * sets of coordinates. The first set of coordinates is used to
         * determine the angle of the starting point relative to the arc's
         * center. The second set of coordinates is used to determine the
         * angle of the end point relative to the arc's center.
         * The arc will always be non-empty and extend counterclockwise
         * from the first point around to the second point.
         *
         * @param x1 The X coordinate of the arc's starting point.
         * @param y1 The Y coordinate of the arc's starting point.
         * @param x2 The X coordinate of the arc's ending point.
         * @param y2 The Y coordinate of the arc's ending point.
         * @since 1.2
         */
        setAngles(x1?: any, y1?: any, x2?: any, y2?: any): any;
        /**
         * Sets the starting angle and angular extent of this arc using
         * two points. The first point is used to determine the angle of
         * the starting point relative to the arc's center.
         * The second point is used to determine the angle of the end point
         * relative to the arc's center.
         * The arc will always be non-empty and extend counterclockwise
         * from the first point around to the second point.
         *
         * @param p1 The <CODE>Point2D</CODE> that defines the arc's
         * starting point.
         * @param p2 The <CODE>Point2D</CODE> that defines the arc's
         * ending point.
         * @since 1.2
         */
        setAngles$java_awt_geom_Point2D$java_awt_geom_Point2D(p1: java.awt.geom.Point2D, p2: java.awt.geom.Point2D): void;
        /**
         * Sets the closure type of this arc to the specified value:
         * <CODE>OPEN</CODE>, <CODE>CHORD</CODE>, or <CODE>PIE</CODE>.
         *
         * @param type The integer constant that represents the closure
         * type of this arc: {@link #OPEN}, {@link #CHORD}, or
         * {@link #PIE}.
         *
         * @throws IllegalArgumentException if <code>type</code> is not
         * 0, 1, or 2.+
         * @see #getArcType
         * @since 1.2
         */
        setArcType(type: number): void;
        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        setFrame(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Returns the high-precision framing rectangle of the arc.  The framing
         * rectangle contains only the part of this <code>Arc2D</code> that is
         * in between the starting and ending angles and contains the pie
         * wedge, if this <code>Arc2D</code> has a <code>PIE</code> closure type.
         * <p>
         * This method differs from the
         * {@link RectangularShape#getBounds() getBounds} in that the
         * <code>getBounds</code> method only returns the bounds of the
         * enclosing ellipse of this <code>Arc2D</code> without considering
         * the starting and ending angles of this <code>Arc2D</code>.
         *
         * @return the <CODE>Rectangle2D</CODE> that represents the arc's
         * framing rectangle.
         * @since 1.2
         */
        getBounds2D(): java.awt.geom.Rectangle2D;
        /**
         * Constructs a <code>Rectangle2D</code> of the appropriate precision
         * to hold the parameters calculated to be the framing rectangle
         * of this arc.
         *
         * @param x The X coordinate of the upper-left corner of the
         * framing rectangle.
         * @param y The Y coordinate of the upper-left corner of the
         * framing rectangle.
         * @param w The width of the framing rectangle.
         * @param h The height of the framing rectangle.
         * @return a <code>Rectangle2D</code> that is the framing rectangle
         * of this arc.
         * @since 1.2
         */
        abstract makeBounds(x: number, y: number, w: number, h: number): java.awt.geom.Rectangle2D;
        static normalizeDegrees(angle: number): number;
        /**
         * Determines whether or not the specified angle is within the
         * angular extents of the arc.
         *
         * @param angle The angle to test.
         *
         * @return <CODE>true</CODE> if the arc contains the angle,
         * <CODE>false</CODE> if the arc doesn't contain the angle.
         * @since 1.2
         */
        containsAngle(angle: number): boolean;
        /**
         * Determines whether or not the specified point is inside the boundary
         * of the arc.
         *
         * @param x The X coordinate of the point to test.
         * @param y The Y coordinate of the point to test.
         *
         * @return <CODE>true</CODE> if the point lies within the bound of
         * the arc, <CODE>false</CODE> if the point lies outside of the
         * arc's bounds.
         * @since 1.2
         */
        contains$double$double(x: number, y: number): boolean;
        /**
         * Determines whether or not the interior of the arc intersects
         * the interior of the specified rectangle.
         *
         * @param x The X coordinate of the rectangle's upper-left corner.
         * @param y The Y coordinate of the rectangle's upper-left corner.
         * @param w The width of the rectangle.
         * @param h The height of the rectangle.
         *
         * @return <CODE>true</CODE> if the arc intersects the rectangle,
         * <CODE>false</CODE> if the arc doesn't intersect the rectangle.
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Determines whether or not the interior of the arc entirely contains
         * the specified rectangle.
         *
         * @param x The X coordinate of the rectangle's upper-left corner.
         * @param y The Y coordinate of the rectangle's upper-left corner.
         * @param w The width of the rectangle.
         * @param h The height of the rectangle.
         *
         * @return <CODE>true</CODE> if the arc contains the rectangle,
         * <CODE>false</CODE> if the arc doesn't contain the rectangle.
         * @since 1.2
         */
        contains$double$double$double$double(x: number, y: number, w: number, h: number): boolean;
        /**
         * Determines whether or not the interior of the arc entirely contains
         * the specified rectangle.
         *
         * @param r The <CODE>Rectangle2D</CODE> to test.
         *
         * @return <CODE>true</CODE> if the arc contains the rectangle,
         * <CODE>false</CODE> if the arc doesn't contain the rectangle.
         * @since 1.2
         */
        contains$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        contains(x?: any, y?: any, w?: any, h?: any, origrect?: any): any;
        /**
         * Returns an iteration object that defines the boundary of the
         * arc.
         * This iterator is multithread safe.
         * <code>Arc2D</code> guarantees that
         * modifications to the geometry of the arc
         * do not affect any iterations of that geometry that
         * are already in process.
         *
         * @param at an optional <CODE>AffineTransform</CODE> to be applied
         * to the coordinates as they are returned in the iteration, or null
         * if the untransformed coordinates are desired.
         *
         * @return A <CODE>PathIterator</CODE> that defines the arc's boundary.
         * @since 1.2
         */
        getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
        /**
         * Returns the hashcode for this <code>Arc2D</code>.
         * @return the hashcode for this <code>Arc2D</code>.
         * @since 1.6
         */
        hashCode(): number;
        /**
         * Determines whether or not the specified <code>Object</code> is
         * equal to this <code>Arc2D</code>.  The specified
         * <code>Object</code> is equal to this <code>Arc2D</code>
         * if it is an instance of <code>Arc2D</code> and if its
         * location, size, arc extents and type are the same as this
         * <code>Arc2D</code>.
         * @param obj  an <code>Object</code> to be compared with this
         * <code>Arc2D</code>.
         * @return  <code>true</code> if <code>obj</code> is an instance
         * of <code>Arc2D</code> and has the same values;
         * <code>false</code> otherwise.
         * @since 1.6
         */
        equals(obj: any): boolean;
    }
    namespace Arc2D {
        /**
         * This class defines an arc specified in {@code float} precision.
         * @since 1.2
         */
        class Float extends java.awt.geom.Arc2D implements java.io.Serializable {
            /**
             * The X coordinate of the upper-left corner of the framing
             * rectangle of the arc.
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of the upper-left corner of the framing
             * rectangle of the arc.
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * The overall width of the full ellipse of which this arc is
             * a partial section (not considering the
             * angular extents).
             * @since 1.2
             * @serial
             */
            width: number;
            /**
             * The overall height of the full ellipse of which this arc is
             * a partial section (not considering the
             * angular extents).
             * @since 1.2
             * @serial
             */
            height: number;
            /**
             * The starting angle of the arc in degrees.
             * @since 1.2
             * @serial
             */
            start: number;
            /**
             * The angular extent of the arc in degrees.
             * @since 1.2
             * @serial
             */
            extent: number;
            /**
             * Constructs a new arc, initialized to the specified location,
             * size, angular extents, and closure type.
             *
             * @param x The X coordinate of the upper-left corner of
             * the arc's framing rectangle.
             * @param y The Y coordinate of the upper-left corner of
             * the arc's framing rectangle.
             * @param w The overall width of the full ellipse of which
             * this arc is a partial section.
             * @param h The overall height of the full ellipse of which this
             * arc is a partial section.
             * @param start The starting angle of the arc in degrees.
             * @param extent The angular extent of the arc in degrees.
             * @param type The closure type for the arc:
             * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
             * @since 1.2
             */
            constructor(x?: any, y?: any, w?: any, h?: any, start?: any, extent?: any, type?: any);
            /**
             * {@inheritDoc}
             * Note that the arc
             * <a href="Arc2D.html#inscribes">partially inscribes</a>
             * the framing rectangle of this {@code RectangularShape}.
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             * Note that the arc
             * <a href="Arc2D.html#inscribes">partially inscribes</a>
             * the framing rectangle of this {@code RectangularShape}.
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             * Note that the arc
             * <a href="Arc2D.html#inscribes">partially inscribes</a>
             * the framing rectangle of this {@code RectangularShape}.
             *
             * @since 1.2
             */
            getWidth(): number;
            /**
             * {@inheritDoc}
             * Note that the arc
             * <a href="Arc2D.html#inscribes">partially inscribes</a>
             * the framing rectangle of this {@code RectangularShape}.
             *
             * @since 1.2
             */
            getHeight(): number;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            getAngleStart(): number;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            getAngleExtent(): number;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            isEmpty(): boolean;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            setArc(x?: any, y?: any, w?: any, h?: any, angSt?: any, angExt?: any, closure?: any): any;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            setAngleStart$double(angSt: number): void;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            setAngleExtent(angExt: number): void;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            makeBounds(x: number, y: number, w: number, h: number): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
        /**
         * This class defines an arc specified in {@code double} precision.
         * @since 1.2
         */
        class Double extends java.awt.geom.Arc2D implements java.io.Serializable {
            /**
             * The X coordinate of the upper-left corner of the framing
             * rectangle of the arc.
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of the upper-left corner of the framing
             * rectangle of the arc.
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * The overall width of the full ellipse of which this arc is
             * a partial section (not considering the angular extents).
             * @since 1.2
             * @serial
             */
            width: number;
            /**
             * The overall height of the full ellipse of which this arc is
             * a partial section (not considering the angular extents).
             * @since 1.2
             * @serial
             */
            height: number;
            /**
             * The starting angle of the arc in degrees.
             * @since 1.2
             * @serial
             */
            start: number;
            /**
             * The angular extent of the arc in degrees.
             * @since 1.2
             * @serial
             */
            extent: number;
            /**
             * Constructs a new arc, initialized to the specified location,
             * size, angular extents, and closure type.
             *
             * @param x The X coordinate of the upper-left corner
             * of the arc's framing rectangle.
             * @param y The Y coordinate of the upper-left corner
             * of the arc's framing rectangle.
             * @param w The overall width of the full ellipse of which this
             * arc is a partial section.
             * @param h The overall height of the full ellipse of which this
             * arc is a partial section.
             * @param start The starting angle of the arc in degrees.
             * @param extent The angular extent of the arc in degrees.
             * @param type The closure type for the arc:
             * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
             * @since 1.2
             */
            constructor(x?: any, y?: any, w?: any, h?: any, start?: any, extent?: any, type?: any);
            /**
             * {@inheritDoc}
             * Note that the arc
             * <a href="Arc2D.html#inscribes">partially inscribes</a>
             * the framing rectangle of this {@code RectangularShape}.
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             * Note that the arc
             * <a href="Arc2D.html#inscribes">partially inscribes</a>
             * the framing rectangle of this {@code RectangularShape}.
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             * Note that the arc
             * <a href="Arc2D.html#inscribes">partially inscribes</a>
             * the framing rectangle of this {@code RectangularShape}.
             *
             * @since 1.2
             */
            getWidth(): number;
            /**
             * {@inheritDoc}
             * Note that the arc
             * <a href="Arc2D.html#inscribes">partially inscribes</a>
             * the framing rectangle of this {@code RectangularShape}.
             *
             * @since 1.2
             */
            getHeight(): number;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            getAngleStart(): number;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            getAngleExtent(): number;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            isEmpty(): boolean;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            setArc(x?: any, y?: any, w?: any, h?: any, angSt?: any, angExt?: any, closure?: any): any;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            setAngleStart$double(angSt: number): void;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            setAngleExtent(angExt: number): void;
            /**
             * {@inheritDoc}
             * @since 1.2
             */
            makeBounds(x: number, y: number, w: number, h: number): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>Ellipse2D</code> class describes an ellipse that is defined by a
     * framing rectangle.
     * <p>
     * This class is only the abstract superclass for all objects which store a 2D
     * ellipse. The actual storage representation of the coordinates is left to the
     * subclass.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class Ellipse2D extends java.awt.geom.RectangularShape {
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for instantiation
         * and provide a number of formats for storing the information necessary to
         * satisfy the various accessor methods below.
         *
         * @see java.awt.geom.Ellipse2D.Float
         * @see java.awt.geom.Ellipse2D.Double
         * @since 1.2
         */
        constructor();
        contains(x?: any, y?: any, w?: any, h?: any, origrect?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double(x: number, y: number): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double$double$double(x: number, y: number, w: number, h: number): boolean;
        /**
         * Returns an iteration object that defines the boundary of this
         * <code>Ellipse2D</code>. The iterator for this class is multi-threaded
         * safe, which means that this <code>Ellipse2D</code> class guarantees that
         * modifications to the geometry of this <code>Ellipse2D</code> object do
         * not affect any iterations of that geometry that are already in process.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @return the <code>PathIterator</code> object that returns the geometry of
         * the outline of this <code>Ellipse2D</code>, one segment at a
         * time.
         * @since 1.2
         */
        getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
        /**
         * Returns the hashcode for this <code>Ellipse2D</code>.
         *
         * @return the hashcode for this <code>Ellipse2D</code>.
         * @since 1.6
         */
        hashCode(): number;
        /**
         * Determines whether or not the specified <code>Object</code> is equal to
         * this <code>Ellipse2D</code>. The specified <code>Object</code> is equal
         * to this <code>Ellipse2D</code> if it is an instance of
         * <code>Ellipse2D</code> and if its location and size are the same as this
         * <code>Ellipse2D</code>.
         *
         * @param obj
         * an <code>Object</code> to be compared with this
         * <code>Ellipse2D</code>.
         * @return <code>true</code> if <code>obj</code> is an instance of
         * <code>Ellipse2D</code> and has the same values;
         * <code>false</code> otherwise.
         * @since 1.6
         */
        equals(obj: any): boolean;
    }
    namespace Ellipse2D {
        /**
         * The <code>Float</code> class defines an ellipse specified in
         * <code>float</code> precision.
         *
         * @since 1.2
         */
        class Float extends java.awt.geom.Ellipse2D implements java.io.Serializable {
            /**
             * The X coordinate of the upper-left corner of the framing rectangle of
             * this {@code Ellipse2D}.
             *
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of the upper-left corner of the framing rectangle of
             * this {@code Ellipse2D}.
             *
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * The overall width of this <code>Ellipse2D</code>.
             *
             * @since 1.2
             * @serial
             */
            width: number;
            /**
             * The overall height of this <code>Ellipse2D</code>.
             *
             * @since 1.2
             * @serial
             */
            height: number;
            /**
             * Constructs and initializes an <code>Ellipse2D</code> from the
             * specified coordinates.
             *
             * @param x
             * the X coordinate of the upper-left corner of the framing
             * rectangle
             * @param y
             * the Y coordinate of the upper-left corner of the framing
             * rectangle
             * @param w
             * the width of the framing rectangle
             * @param h
             * the height of the framing rectangle
             * @since 1.2
             */
            constructor(x?: any, y?: any, w?: any, h?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getWidth(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getHeight(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            isEmpty(): boolean;
            /**
             * Sets the location and size of the framing rectangle of this
             * <code>Shape</code> to the specified rectangular values.
             *
             * @param x
             * the X coordinate of the upper-left corner of the specified
             * rectangular shape
             * @param y
             * the Y coordinate of the upper-left corner of the specified
             * rectangular shape
             * @param w
             * the width of the specified rectangular shape
             * @param h
             * the height of the specified rectangular shape
             * @since 1.2
             */
            setFrame(x?: any, y?: any, w?: any, h?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setFrame$double$double$double$double(x: number, y: number, w: number, h: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
        /**
         * The <code>Double</code> class defines an ellipse specified in
         * <code>double</code> precision.
         *
         * @since 1.2
         */
        class Double extends java.awt.geom.Ellipse2D implements java.io.Serializable {
            /**
             * The X coordinate of the upper-left corner of the framing rectangle of
             * this {@code Ellipse2D}.
             *
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of the upper-left corner of the framing rectangle of
             * this {@code Ellipse2D}.
             *
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * The overall width of this <code>Ellipse2D</code>.
             *
             * @since 1.2
             * @serial
             */
            width: number;
            /**
             * The overall height of the <code>Ellipse2D</code>.
             *
             * @since 1.2
             * @serial
             */
            height: number;
            /**
             * Constructs and initializes an <code>Ellipse2D</code> from the
             * specified coordinates.
             *
             * @param x
             * the X coordinate of the upper-left corner of the framing
             * rectangle
             * @param y
             * the Y coordinate of the upper-left corner of the framing
             * rectangle
             * @param w
             * the width of the framing rectangle
             * @param h
             * the height of the framing rectangle
             * @since 1.2
             */
            constructor(x?: any, y?: any, w?: any, h?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getWidth(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getHeight(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            isEmpty(): boolean;
            /**
             * Sets the location and size of the framing rectangle of this
             * <code>Shape</code> to the specified rectangular values.
             *
             * @param x
             * the X coordinate of the upper-left corner of the specified
             * rectangular shape
             * @param y
             * the Y coordinate of the upper-left corner of the specified
             * rectangular shape
             * @param w
             * the width of the specified rectangular shape
             * @param h
             * the height of the specified rectangular shape
             * @since 1.2
             */
            setFrame(x?: any, y?: any, w?: any, h?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setFrame$double$double$double$double(x: number, y: number, w: number, h: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>Rectangle2D</code> class describes a rectangle defined by a
     * location {@code (x,y)} and dimension {@code (w x h)}.
     * <p>
     * This class is only the abstract superclass for all objects that store a 2D
     * rectangle. The actual storage representation of the coordinates is left to
     * the subclass.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class Rectangle2D extends java.awt.geom.RectangularShape {
        /**
         * The bitmask that indicates that a point lies to the left of this
         * <code>Rectangle2D</code>.
         *
         * @since 1.2
         */
        static OUT_LEFT: number;
        /**
         * The bitmask that indicates that a point lies above this
         * <code>Rectangle2D</code>.
         *
         * @since 1.2
         */
        static OUT_TOP: number;
        /**
         * The bitmask that indicates that a point lies to the right of this
         * <code>Rectangle2D</code>.
         *
         * @since 1.2
         */
        static OUT_RIGHT: number;
        /**
         * The bitmask that indicates that a point lies below this
         * <code>Rectangle2D</code>.
         *
         * @since 1.2
         */
        static OUT_BOTTOM: number;
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for instantiation
         * and provide a number of formats for storing the information necessary to
         * satisfy the various accessor methods below.
         *
         * @see java.awt.geom.Rectangle2D.Float
         * @see java.awt.geom.Rectangle2D.Double
         * @see java.awt.Rectangle
         * @since 1.2
         */
        constructor();
        /**
         * Sets the location and size of this <code>Rectangle2D</code> to the
         * specified <code>float</code> values.
         *
         * @param x
         * the X coordinate of the upper-left corner of this
         * <code>Rectangle2D</code>
         * @param y
         * the Y coordinate of the upper-left corner of this
         * <code>Rectangle2D</code>
         * @param w
         * the width of this <code>Rectangle2D</code>
         * @param h
         * the height of this <code>Rectangle2D</code>
         * @since 1.2
         */
        setRect(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Sets the location and size of this <code>Rectangle2D</code> to the
         * specified <code>double</code> values.
         *
         * @param x
         * the X coordinate of the upper-left corner of this
         * <code>Rectangle2D</code>
         * @param y
         * the Y coordinate of the upper-left corner of this
         * <code>Rectangle2D</code>
         * @param w
         * the width of this <code>Rectangle2D</code>
         * @param h
         * the height of this <code>Rectangle2D</code>
         * @since 1.2
         */
        setRect$double$double$double$double(x: number, y: number, w: number, h: number): void;
        /**
         * Sets this <code>Rectangle2D</code> to be the same as the specified
         * <code>Rectangle2D</code>.
         *
         * @param r
         * the specified <code>Rectangle2D</code>
         * @since 1.2
         */
        setRect$java_awt_geom_Rectangle2D(r: Rectangle2D): void;
        /**
         * Tests if the specified line segment intersects the interior of this
         * <code>Rectangle2D</code>.
         *
         * @param x1
         * the X coordinate of the start point of the specified line
         * segment
         * @param y1
         * the Y coordinate of the start point of the specified line
         * segment
         * @param x2
         * the X coordinate of the end point of the specified line
         * segment
         * @param y2
         * the Y coordinate of the end point of the specified line
         * segment
         * @return <code>true</code> if the specified line segment intersects the
         * interior of this <code>Rectangle2D</code>; <code>false</code>
         * otherwise.
         * @since 1.2
         */
        intersectsLine(x1?: any, y1?: any, x2?: any, y2?: any): any;
        /**
         * Tests if the specified line segment intersects the interior of this
         * <code>Rectangle2D</code>.
         *
         * @param l
         * the specified {@link Line2D} to test for intersection with the
         * interior of this <code>Rectangle2D</code>
         * @return <code>true</code> if the specified <code>Line2D</code> intersects
         * the interior of this <code>Rectangle2D</code>; <code>false</code>
         * otherwise.
         * @since 1.2
         */
        intersectsLine$java_awt_geom_Line2D(l: java.awt.geom.Line2D): boolean;
        /**
         * Determines where the specified coordinates lie with respect to this
         * <code>Rectangle2D</code>. This method computes a binary OR of the
         * appropriate mask values indicating, for each side of this
         * <code>Rectangle2D</code>, whether or not the specified coordinates are on
         * the same side of the edge as the rest of this <code>Rectangle2D</code>.
         *
         * @param x
         * the specified X coordinate
         * @param y
         * the specified Y coordinate
         * @return the logical OR of all appropriate out codes.
         * @see #OUT_LEFT
         * @see #OUT_TOP
         * @see #OUT_RIGHT
         * @see #OUT_BOTTOM
         * @since 1.2
         */
        outcode(x?: any, y?: any): any;
        /**
         * Determines where the specified {@link Point2D} lies with respect to this
         * <code>Rectangle2D</code>. This method computes a binary OR of the
         * appropriate mask values indicating, for each side of this
         * <code>Rectangle2D</code>, whether or not the specified
         * <code>Point2D</code> is on the same side of the edge as the rest of this
         * <code>Rectangle2D</code>.
         *
         * @param p
         * the specified <code>Point2D</code>
         * @return the logical OR of all appropriate out codes.
         * @see #OUT_LEFT
         * @see #OUT_TOP
         * @see #OUT_RIGHT
         * @see #OUT_BOTTOM
         * @since 1.2
         */
        outcode$java_awt_geom_Point2D(p: java.awt.geom.Point2D): number;
        setFrame(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Sets the location and size of the outer bounds of this
         * <code>Rectangle2D</code> to the specified rectangular values.
         *
         * @param x
         * the X coordinate of the upper-left corner of this
         * <code>Rectangle2D</code>
         * @param y
         * the Y coordinate of the upper-left corner of this
         * <code>Rectangle2D</code>
         * @param w
         * the width of this <code>Rectangle2D</code>
         * @param h
         * the height of this <code>Rectangle2D</code>
         * @since 1.2
         */
        setFrame$double$double$double$double(x: number, y: number, w: number, h: number): void;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        getBounds2D(): Rectangle2D;
        contains(x?: any, y?: any, w?: any, h?: any, origrect?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double(x: number, y: number): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double$double$double(x: number, y: number, w: number, h: number): boolean;
        /**
         * Returns a new <code>Rectangle2D</code> object representing the
         * intersection of this <code>Rectangle2D</code> with the specified
         * <code>Rectangle2D</code>.
         *
         * @param r
         * the <code>Rectangle2D</code> to be intersected with this
         * <code>Rectangle2D</code>
         * @return the largest <code>Rectangle2D</code> contained in both the
         * specified <code>Rectangle2D</code> and in this
         * <code>Rectangle2D</code>.
         * @since 1.2
         */
        abstract createIntersection(r: Rectangle2D): Rectangle2D;
        /**
         * Intersects the pair of specified source <code>Rectangle2D</code> objects
         * and puts the result into the specified destination
         * <code>Rectangle2D</code> object. One of the source rectangles can also be
         * the destination to avoid creating a third Rectangle2D object, but in this
         * case the original points of this source rectangle will be overwritten by
         * this method.
         *
         * @param src1
         * the first of a pair of <code>Rectangle2D</code> objects to be
         * intersected with each other
         * @param src2
         * the second of a pair of <code>Rectangle2D</code> objects to be
         * intersected with each other
         * @param dest
         * the <code>Rectangle2D</code> that holds the results of the
         * intersection of <code>src1</code> and <code>src2</code>
         * @since 1.2
         */
        static intersect(src1: Rectangle2D, src2: Rectangle2D, dest: Rectangle2D): void;
        /**
         * Returns a new <code>Rectangle2D</code> object representing the union of
         * this <code>Rectangle2D</code> with the specified <code>Rectangle2D</code>
         * .
         *
         * @param r
         * the <code>Rectangle2D</code> to be combined with this
         * <code>Rectangle2D</code>
         * @return the smallest <code>Rectangle2D</code> containing both the
         * specified <code>Rectangle2D</code> and this
         * <code>Rectangle2D</code>.
         * @since 1.2
         */
        abstract createUnion(r: Rectangle2D): Rectangle2D;
        /**
         * Unions the pair of source <code>Rectangle2D</code> objects and puts the
         * result into the specified destination <code>Rectangle2D</code> object.
         * One of the source rectangles can also be the destination to avoid
         * creating a third Rectangle2D object, but in this case the original points
         * of this source rectangle will be overwritten by this method.
         *
         * @param src1
         * the first of a pair of <code>Rectangle2D</code> objects to be
         * combined with each other
         * @param src2
         * the second of a pair of <code>Rectangle2D</code> objects to be
         * combined with each other
         * @param dest
         * the <code>Rectangle2D</code> that holds the results of the
         * union of <code>src1</code> and <code>src2</code>
         * @since 1.2
         */
        static union(src1: Rectangle2D, src2: Rectangle2D, dest: Rectangle2D): void;
        add(newx?: any, newy?: any): any;
        /**
         * Adds a point, specified by the double precision arguments
         * <code>newx</code> and <code>newy</code>, to this <code>Rectangle2D</code>
         * . The resulting <code>Rectangle2D</code> is the smallest
         * <code>Rectangle2D</code> that contains both the original
         * <code>Rectangle2D</code> and the specified point.
         * <p>
         * After adding a point, a call to <code>contains</code> with the added
         * point as an argument does not necessarily return <code>true</code>. The
         * <code>contains</code> method does not return <code>true</code> for points
         * on the right or bottom edges of a rectangle. Therefore, if the added
         * point falls on the left or bottom edge of the enlarged rectangle,
         * <code>contains</code> returns <code>false</code> for that point.
         *
         * @param newx
         * the X coordinate of the new point
         * @param newy
         * the Y coordinate of the new point
         * @since 1.2
         */
        add$double$double(newx: number, newy: number): void;
        /**
         * Adds the <code>Point2D</code> object <code>pt</code> to this
         * <code>Rectangle2D</code>. The resulting <code>Rectangle2D</code> is the
         * smallest <code>Rectangle2D</code> that contains both the original
         * <code>Rectangle2D</code> and the specified <code>Point2D</code>.
         * <p>
         * After adding a point, a call to <code>contains</code> with the added
         * point as an argument does not necessarily return <code>true</code>. The
         * <code>contains</code> method does not return <code>true</code> for points
         * on the right or bottom edges of a rectangle. Therefore, if the added
         * point falls on the left or bottom edge of the enlarged rectangle,
         * <code>contains</code> returns <code>false</code> for that point.
         *
         * @param pt
         * the new <code>Point2D</code> to add to this
         * <code>Rectangle2D</code>.
         * @since 1.2
         */
        add$java_awt_geom_Point2D(pt: java.awt.geom.Point2D): void;
        /**
         * Adds a <code>Rectangle2D</code> object to this <code>Rectangle2D</code>.
         * The resulting <code>Rectangle2D</code> is the union of the two
         * <code>Rectangle2D</code> objects.
         *
         * @param r
         * the <code>Rectangle2D</code> to add to this
         * <code>Rectangle2D</code>.
         * @since 1.2
         */
        add$java_awt_geom_Rectangle2D(r: Rectangle2D): void;
        /**
         * Returns an iteration object that defines the boundary of this
         * <code>Rectangle2D</code>. The iterator for this class is multi-threaded
         * safe, which means that this <code>Rectangle2D</code> class guarantees
         * that modifications to the geometry of this <code>Rectangle2D</code>
         * object do not affect any iterations of that geometry that are already in
         * process.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @return the <code>PathIterator</code> object that returns the geometry of
         * the outline of this <code>Rectangle2D</code>, one segment at a
         * time.
         * @since 1.2
         */
        getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
        /**
         * Returns an iteration object that defines the boundary of the flattened
         * <code>Rectangle2D</code>. Since rectangles are already flat, the
         * <code>flatness</code> parameter is ignored. The iterator for this class
         * is multi-threaded safe, which means that this <code>Rectangle2D</code>
         * class guarantees that modifications to the geometry of this
         * <code>Rectangle2D</code> object do not affect any iterations of that
         * geometry that are already in process.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @param flatness
         * the maximum distance that the line segments used to
         * approximate the curved segments are allowed to deviate from
         * any point on the original curve. Since rectangles are already
         * flat, the <code>flatness</code> parameter is ignored.
         * @return the <code>PathIterator</code> object that returns the geometry of
         * the outline of this <code>Rectangle2D</code>, one segment at a
         * time.
         * @since 1.2
         */
        getPathIterator(at?: any, flatness?: any): any;
        /**
         * Returns the hashcode for this <code>Rectangle2D</code>.
         *
         * @return the hashcode for this <code>Rectangle2D</code>.
         * @since 1.2
         */
        hashCode(): number;
        /**
         * Determines whether or not the specified <code>Object</code> is equal to
         * this <code>Rectangle2D</code>. The specified <code>Object</code> is equal
         * to this <code>Rectangle2D</code> if it is an instance of
         * <code>Rectangle2D</code> and if its location and size are the same as
         * this <code>Rectangle2D</code>.
         *
         * @param obj
         * an <code>Object</code> to be compared with this
         * <code>Rectangle2D</code>.
         * @return <code>true</code> if <code>obj</code> is an instance of
         * <code>Rectangle2D</code> and has the same values;
         * <code>false</code> otherwise.
         * @since 1.2
         */
        equals(obj: any): boolean;
    }
    namespace Rectangle2D {
        /**
         * The <code>Float</code> class defines a rectangle specified in float
         * coordinates.
         *
         * @since 1.2
         */
        class Float extends java.awt.geom.Rectangle2D implements java.io.Serializable {
            /**
             * The X coordinate of this <code>Rectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of this <code>Rectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * The width of this <code>Rectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            width: number;
            /**
             * The height of this <code>Rectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            height: number;
            /**
             * Constructs and initializes a <code>Rectangle2D</code> from the
             * specified <code>float</code> coordinates.
             *
             * @param x
             * the X coordinate of the upper-left corner of the newly
             * constructed <code>Rectangle2D</code>
             * @param y
             * the Y coordinate of the upper-left corner of the newly
             * constructed <code>Rectangle2D</code>
             * @param w
             * the width of the newly constructed
             * <code>Rectangle2D</code>
             * @param h
             * the height of the newly constructed
             * <code>Rectangle2D</code>
             * @since 1.2
             */
            constructor(x?: any, y?: any, w?: any, h?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getWidth(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getHeight(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            isEmpty(): boolean;
            /**
             * Sets the location and size of this <code>Rectangle2D</code> to the
             * specified <code>float</code> values.
             *
             * @param x
             * the X coordinate of the upper-left corner of this
             * <code>Rectangle2D</code>
             * @param y
             * the Y coordinate of the upper-left corner of this
             * <code>Rectangle2D</code>
             * @param w
             * the width of this <code>Rectangle2D</code>
             * @param h
             * the height of this <code>Rectangle2D</code>
             * @since 1.2
             */
            setRect(x?: any, y?: any, w?: any, h?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setRect$double$double$double$double(x: number, y: number, w: number, h: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setRect$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            outcode(x?: any, y?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            createIntersection(r: java.awt.geom.Rectangle2D): java.awt.geom.Rectangle2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            createUnion(r: java.awt.geom.Rectangle2D): java.awt.geom.Rectangle2D;
            /**
             * Returns the <code>String</code> representation of this
             * <code>Rectangle2D</code>.
             *
             * @return a <code>String</code> representing this
             * <code>Rectangle2D</code>.
             * @since 1.2
             */
            toString(): string;
            static serialVersionUID: number;
        }
        /**
         * The <code>Double</code> class defines a rectangle specified in double
         * coordinates.
         *
         * @since 1.2
         */
        class Double extends java.awt.geom.Rectangle2D implements java.io.Serializable {
            /**
             * The X coordinate of this <code>Rectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of this <code>Rectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * The width of this <code>Rectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            width: number;
            /**
             * The height of this <code>Rectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            height: number;
            /**
             * Constructs and initializes a <code>Rectangle2D</code> from the
             * specified <code>double</code> coordinates.
             *
             * @param x
             * the X coordinate of the upper-left corner of the newly
             * constructed <code>Rectangle2D</code>
             * @param y
             * the Y coordinate of the upper-left corner of the newly
             * constructed <code>Rectangle2D</code>
             * @param w
             * the width of the newly constructed
             * <code>Rectangle2D</code>
             * @param h
             * the height of the newly constructed
             * <code>Rectangle2D</code>
             * @since 1.2
             */
            constructor(x?: any, y?: any, w?: any, h?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getWidth(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getHeight(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            isEmpty(): boolean;
            /**
             * Sets the location and size of this <code>Rectangle2D</code> to the
             * specified <code>float</code> values.
             *
             * @param x
             * the X coordinate of the upper-left corner of this
             * <code>Rectangle2D</code>
             * @param y
             * the Y coordinate of the upper-left corner of this
             * <code>Rectangle2D</code>
             * @param w
             * the width of this <code>Rectangle2D</code>
             * @param h
             * the height of this <code>Rectangle2D</code>
             * @since 1.2
             */
            setRect(x?: any, y?: any, w?: any, h?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setRect$double$double$double$double(x: number, y: number, w: number, h: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setRect$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            outcode(x?: any, y?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            createIntersection(r: java.awt.geom.Rectangle2D): java.awt.geom.Rectangle2D;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            createUnion(r: java.awt.geom.Rectangle2D): java.awt.geom.Rectangle2D;
            /**
             * Returns the <code>String</code> representation of this
             * <code>Rectangle2D</code>.
             *
             * @return a <code>String</code> representing this
             * <code>Rectangle2D</code>.
             * @since 1.2
             */
            toString(): string;
            static serialVersionUID: number;
        }
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>RoundRectangle2D</code> class defines a rectangle with rounded
     * corners defined by a location {@code (x,y)}, a dimension {@code (w x h)}, and
     * the width and height of an arc with which to round the corners.
     * <p>
     * This class is the abstract superclass for all objects that store a 2D rounded
     * rectangle. The actual storage representation of the coordinates is left to
     * the subclass.
     *
     * @author Jim Graham
     * @since 1.2
     */
    abstract class RoundRectangle2D extends java.awt.geom.RectangularShape {
        /**
         * This is an abstract class that cannot be instantiated directly.
         * Type-specific implementation subclasses are available for instantiation
         * and provide a number of formats for storing the information necessary to
         * satisfy the various accessor methods below.
         *
         * @see java.awt.geom.RoundRectangle2D.Float
         * @see java.awt.geom.RoundRectangle2D.Double
         * @since 1.2
         */
        constructor();
        /**
         * Gets the width of the arc that rounds off the corners.
         *
         * @return the width of the arc that rounds off the corners of this
         * <code>RoundRectangle2D</code>.
         * @since 1.2
         */
        abstract getArcWidth(): number;
        /**
         * Gets the height of the arc that rounds off the corners.
         *
         * @return the height of the arc that rounds off the corners of this
         * <code>RoundRectangle2D</code>.
         * @since 1.2
         */
        abstract getArcHeight(): number;
        /**
         * Sets the location, size, and corner radii of this
         * <code>RoundRectangle2D</code> to the specified <code>float</code>
         * values.
         *
         * @param x
         * the X coordinate to which to set the location of this
         * <code>RoundRectangle2D</code>
         * @param y
         * the Y coordinate to which to set the location of this
         * <code>RoundRectangle2D</code>
         * @param w
         * the width to which to set this
         * <code>RoundRectangle2D</code>
         * @param h
         * the height to which to set this
         * <code>RoundRectangle2D</code>
         * @param arcw
         * the width to which to set the arc of this
         * <code>RoundRectangle2D</code>
         * @param arch
         * the height to which to set the arc of this
         * <code>RoundRectangle2D</code>
         * @since 1.2
         */
        setRoundRect(x?: any, y?: any, w?: any, h?: any, arcw?: any, arch?: any): any;
        /**
         * Sets the location, size, and corner radii of this
         * <code>RoundRectangle2D</code> to the specified <code>double</code>
         * values.
         *
         * @param x
         * the X coordinate to which to set the location of this
         * <code>RoundRectangle2D</code>
         * @param y
         * the Y coordinate to which to set the location of this
         * <code>RoundRectangle2D</code>
         * @param w
         * the width to which to set this <code>RoundRectangle2D</code>
         * @param h
         * the height to which to set this <code>RoundRectangle2D</code>
         * @param arcWidth
         * the width to which to set the arc of this
         * <code>RoundRectangle2D</code>
         * @param arcHeight
         * the height to which to set the arc of this
         * <code>RoundRectangle2D</code>
         * @since 1.2
         */
        setRoundRect$double$double$double$double$double$double(x: number, y: number, w: number, h: number, arcWidth: number, arcHeight: number): void;
        /**
         * Sets this <code>RoundRectangle2D</code> to be the same as the specified
         * <code>RoundRectangle2D</code>.
         *
         * @param rr
         * the specified <code>RoundRectangle2D</code>
         * @since 1.2
         */
        setRoundRect$java_awt_geom_RoundRectangle2D(rr: RoundRectangle2D): void;
        setFrame(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        setFrame$double$double$double$double(x: number, y: number, w: number, h: number): void;
        contains(x?: any, y?: any, w?: any, h?: any, origrect?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double(x: number, y: number): boolean;
        classify(coord: number, left: number, right: number, arcsize: number): number;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double$double$double(x: number, y: number, w: number, h: number): boolean;
        /**
         * Returns an iteration object that defines the boundary of this
         * <code>RoundRectangle2D</code>. The iterator for this class is
         * multi-threaded safe, which means that this <code>RoundRectangle2D</code>
         * class guarantees that modifications to the geometry of this
         * <code>RoundRectangle2D</code> object do not affect any iterations of that
         * geometry that are already in process.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @return the <code>PathIterator</code> object that returns the geometry of
         * the outline of this <code>RoundRectangle2D</code>, one segment at
         * a time.
         * @since 1.2
         */
        getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
        /**
         * Returns the hashcode for this <code>RoundRectangle2D</code>.
         *
         * @return the hashcode for this <code>RoundRectangle2D</code>.
         * @since 1.6
         */
        hashCode(): number;
        /**
         * Determines whether or not the specified <code>Object</code> is equal to
         * this <code>RoundRectangle2D</code>. The specified <code>Object</code> is
         * equal to this <code>RoundRectangle2D</code> if it is an instance of
         * <code>RoundRectangle2D</code> and if its location, size, and corner arc
         * dimensions are the same as this <code>RoundRectangle2D</code>.
         *
         * @param obj
         * an <code>Object</code> to be compared with this
         * <code>RoundRectangle2D</code>.
         * @return <code>true</code> if <code>obj</code> is an instance of
         * <code>RoundRectangle2D</code> and has the same values;
         * <code>false</code> otherwise.
         * @since 1.6
         */
        equals(obj: any): boolean;
    }
    namespace RoundRectangle2D {
        /**
         * The <code>Float</code> class defines a rectangle with rounded corners all
         * specified in <code>float</code> coordinates.
         *
         * @since 1.2
         */
        class Float extends java.awt.geom.RoundRectangle2D implements java.io.Serializable {
            /**
             * The X coordinate of this <code>RoundRectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of this <code>RoundRectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * The width of this <code>RoundRectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            width: number;
            /**
             * The height of this <code>RoundRectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            height: number;
            /**
             * The width of the arc that rounds off the corners.
             *
             * @since 1.2
             * @serial
             */
            arcwidth: number;
            /**
             * The height of the arc that rounds off the corners.
             *
             * @since 1.2
             * @serial
             */
            archeight: number;
            /**
             * Constructs and initializes a <code>RoundRectangle2D</code> from the
             * specified <code>float</code> coordinates.
             *
             * @param x
             * the X coordinate of the newly constructed
             * <code>RoundRectangle2D</code>
             * @param y
             * the Y coordinate of the newly constructed
             * <code>RoundRectangle2D</code>
             * @param w
             * the width to which to set the newly constructed
             * <code>RoundRectangle2D</code>
             * @param h
             * the height to which to set the newly constructed
             * <code>RoundRectangle2D</code>
             * @param arcw
             * the width of the arc to use to round off the corners of
             * the newly constructed <code>RoundRectangle2D</code>
             * @param arch
             * the height of the arc to use to round off the corners of
             * the newly constructed <code>RoundRectangle2D</code>
             * @since 1.2
             */
            constructor(x?: any, y?: any, w?: any, h?: any, arcw?: any, arch?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getWidth(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getHeight(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getArcWidth(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getArcHeight(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            isEmpty(): boolean;
            /**
             * Sets the location, size, and corner radii of this
             * <code>RoundRectangle2D</code> to the specified <code>float</code>
             * values.
             *
             * @param x
             * the X coordinate to which to set the location of this
             * <code>RoundRectangle2D</code>
             * @param y
             * the Y coordinate to which to set the location of this
             * <code>RoundRectangle2D</code>
             * @param w
             * the width to which to set this
             * <code>RoundRectangle2D</code>
             * @param h
             * the height to which to set this
             * <code>RoundRectangle2D</code>
             * @param arcw
             * the width to which to set the arc of this
             * <code>RoundRectangle2D</code>
             * @param arch
             * the height to which to set the arc of this
             * <code>RoundRectangle2D</code>
             * @since 1.2
             */
            setRoundRect(x?: any, y?: any, w?: any, h?: any, arcw?: any, arch?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setRoundRect$double$double$double$double$double$double(x: number, y: number, w: number, h: number, arcw: number, arch: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setRoundRect$java_awt_geom_RoundRectangle2D(rr: java.awt.geom.RoundRectangle2D): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
        /**
         * The <code>Double</code> class defines a rectangle with rounded corners
         * all specified in <code>double</code> coordinates.
         *
         * @since 1.2
         */
        class Double extends java.awt.geom.RoundRectangle2D implements java.io.Serializable {
            /**
             * The X coordinate of this <code>RoundRectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            x: number;
            /**
             * The Y coordinate of this <code>RoundRectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            y: number;
            /**
             * The width of this <code>RoundRectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            width: number;
            /**
             * The height of this <code>RoundRectangle2D</code>.
             *
             * @since 1.2
             * @serial
             */
            height: number;
            /**
             * The width of the arc that rounds off the corners.
             *
             * @since 1.2
             * @serial
             */
            arcwidth: number;
            /**
             * The height of the arc that rounds off the corners.
             *
             * @since 1.2
             * @serial
             */
            archeight: number;
            /**
             * Constructs and initializes a <code>RoundRectangle2D</code> from the
             * specified <code>double</code> coordinates.
             *
             * @param x
             * the X coordinate of the newly constructed
             * <code>RoundRectangle2D</code>
             * @param y
             * the Y coordinate of the newly constructed
             * <code>RoundRectangle2D</code>
             * @param w
             * the width to which to set the newly constructed
             * <code>RoundRectangle2D</code>
             * @param h
             * the height to which to set the newly constructed
             * <code>RoundRectangle2D</code>
             * @param arcw
             * the width of the arc to use to round off the corners of
             * the newly constructed <code>RoundRectangle2D</code>
             * @param arch
             * the height of the arc to use to round off the corners of
             * the newly constructed <code>RoundRectangle2D</code>
             * @since 1.2
             */
            constructor(x?: any, y?: any, w?: any, h?: any, arcw?: any, arch?: any);
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getX(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getY(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getWidth(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getHeight(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getArcWidth(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getArcHeight(): number;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            isEmpty(): boolean;
            /**
             * Sets the location, size, and corner radii of this
             * <code>RoundRectangle2D</code> to the specified <code>float</code>
             * values.
             *
             * @param x
             * the X coordinate to which to set the location of this
             * <code>RoundRectangle2D</code>
             * @param y
             * the Y coordinate to which to set the location of this
             * <code>RoundRectangle2D</code>
             * @param w
             * the width to which to set this
             * <code>RoundRectangle2D</code>
             * @param h
             * the height to which to set this
             * <code>RoundRectangle2D</code>
             * @param arcw
             * the width to which to set the arc of this
             * <code>RoundRectangle2D</code>
             * @param arch
             * the height to which to set the arc of this
             * <code>RoundRectangle2D</code>
             * @since 1.2
             */
            setRoundRect(x?: any, y?: any, w?: any, h?: any, arcw?: any, arch?: any): any;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setRoundRect$double$double$double$double$double$double(x: number, y: number, w: number, h: number, arcw: number, arch: number): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            setRoundRect$java_awt_geom_RoundRectangle2D(rr: java.awt.geom.RoundRectangle2D): void;
            /**
             * {@inheritDoc}
             *
             * @since 1.2
             */
            getBounds2D(): java.awt.geom.Rectangle2D;
            static serialVersionUID: number;
        }
    }
}
declare namespace java.awt {
    class WebGraphics2D extends java.awt.Graphics {
        private canvas;
        private context;
        constructor(canvas: HTMLCanvasElement);
        drawString(s: string, x: number, y: number): void;
        getCanvas(): HTMLCanvasElement;
        getContext(): CanvasRenderingContext2D;
    }
}
declare namespace java.io {
    class LocalStorageFileSystem extends java.io.FileSystem {
        private PREFIX;
        roots: java.io.File[];
        getSeparator(): string;
        getPathSeparator(): string;
        normalize(pathname?: any, len?: any, off?: any): any;
        normalize$java_lang_String(pathname: string): string;
        prefixLength(pathname: string): number;
        resolve(parent?: any, child?: any): any;
        getDefaultParent(): string;
        fromURIPath(path: string): string;
        isAbsolute(f: java.io.File): boolean;
        resolve$java_io_File(f: java.io.File): string;
        canonicalize(path: string): string;
        getBooleanAttributes(f: java.io.File): number;
        checkAccess(f: java.io.File, access: number): boolean;
        setPermission(f: java.io.File, access: number, enable: boolean, owneronly: boolean): boolean;
        getLastModifiedTime(f: java.io.File): number;
        getLength(f: java.io.File): number;
        clear(): void;
        getKey(pathname: string): string;
        createFileExclusively(pathname: string): boolean;
        hasEntry(pathname: string): boolean;
        getEntry(pathname: string): LocalStorageFileSystem.Entry;
        getDirectoryEntry(pathname: string): LocalStorageFileSystem.DirectoryEntry;
        putEntry(pathname: string, entry: LocalStorageFileSystem.Entry): void;
        getChildEntries(pathname: string): Array<string>;
        removeEntry(pathname: string): void;
        delete(f: java.io.File): boolean;
        list(f: java.io.File): string[];
        createDirectory(f: java.io.File): boolean;
        rename(f1: java.io.File, f2: java.io.File): boolean;
        setLastModifiedTime(f: java.io.File, time: number): boolean;
        setReadOnly(f: java.io.File): boolean;
        listRoots(): java.io.File[];
        getSpace(f: java.io.File, t: number): number;
        compare(f1: java.io.File, f2: java.io.File): number;
        hashCode(f: java.io.File): number;
        constructor();
    }
    namespace LocalStorageFileSystem {
        interface Entry {
            attributes: number;
            access: number;
            data: string;
            lastModifiedTime: number;
            length: number;
        }
        interface DirectoryEntry extends LocalStorageFileSystem.Entry {
            entries: string[];
        }
    }
}
declare namespace java.io {
    /**
     * A specialized {@link InputStream } for reading the contents of a byte array.
     *
     * @see ByteArrayOutputStream
     */
    class ByteArrayInputStream extends java.io.InputStream {
        /**
         * The {@code byte} array containing the bytes to stream over.
         */
        buf: number[];
        /**
         * The current position within the byte array.
         */
        pos: number;
        /**
         * The current mark position. Initially set to 0 or the <code>offset</code>
         * parameter within the constructor.
         */
        _mark: number;
        /**
         * The total number of bytes initially available in the byte array
         * {@code buf}.
         */
        count: number;
        /**
         * Constructs a new {@code ByteArrayInputStream} on the byte array
         * {@code buf} with the initial position set to {@code offset} and the
         * number of bytes available set to {@code offset} + {@code length}.
         *
         * @param buf
         * the byte array to stream over.
         * @param offset
         * the initial position in {@code buf} to start streaming from.
         * @param length
         * the number of bytes available for streaming.
         */
        constructor(buf: number[], offset?: number, length?: number);
        /**
         * Returns the number of remaining bytes.
         *
         * @return {@code count - pos}
         */
        available(): number;
        /**
         * Closes this stream and frees resources associated with this stream.
         *
         * @throws IOException
         * if an I/O error occurs while closing this stream.
         */
        close(): void;
        /**
         * Sets a mark position in this ByteArrayInputStream. The parameter
         * {@code readlimit} is ignored. Sending {@code reset()} will reposition the
         * stream back to the marked position.
         *
         * @param readlimit
         * ignored.
         * @see #markSupported()
         * @see #reset()
         */
        mark(readlimit: number): void;
        /**
         * Indicates whether this stream supports the {@code mark()} and
         * {@code reset()} methods. Returns {@code true} since this class supports
         * these methods.
         *
         * @return always {@code true}.
         * @see #mark(int)
         * @see #reset()
         */
        markSupported(): boolean;
        /**
         * Reads a single byte from the source byte array and returns it as an
         * integer in the range from 0 to 255. Returns -1 if the end of the source
         * array has been reached.
         *
         * @return the byte read or -1 if the end of this stream has been reached.
         */
        read$(): number;
        read(buffer?: any, byteOffset?: any, byteCount?: any): any;
        /**
         * Resets this stream to the last marked location. This implementation
         * resets the position to either the marked position, the start position
         * supplied in the constructor or 0 if neither has been provided.
         *
         * @see #mark(int)
         */
        reset(): void;
        /**
         * Skips {@code byteCount} bytes in this InputStream. Subsequent calls to
         * {@code read} will not return these bytes unless {@code reset} is used.
         * This implementation skips {@code byteCount} number of bytes in the target
         * stream. It does nothing and returns 0 if {@code byteCount} is negative.
         *
         * @return the number of bytes actually skipped.
         */
        skip(byteCount: number): number;
    }
}
declare namespace java.io {
    /**
     * JSweet implementation based on a local storage FS.
     */
    class FileInputStream extends java.io.InputStream {
        private content;
        private index;
        constructor(name?: any);
        read$(): number;
        private readBytes(b, off, len);
        read$byte_A(b: number[]): number;
        read(b?: any, off?: any, len?: any): any;
        skip(n: number): number;
        available(): number;
        close(): void;
    }
}
declare namespace java.io {
    /**
     * Wraps an existing {@link InputStream} and performs some transformation on
     * the input data while it is being read. Transformations can be anything from a
     * simple byte-wise filtering input data to an on-the-fly compression or
     * decompression of the underlying stream. Input streams that wrap another input
     * stream and provide some additional functionality on top of it usually inherit
     * from this class.
     *
     * @see FilterOutputStream
     */
    class FilterInputStream extends java.io.InputStream {
        /**
         * The source input stream that is filtered.
         */
        in: java.io.InputStream;
        /**
         * Constructs a new {@code FilterInputStream} with the specified input
         * stream as source.
         *
         * <p><strong>Warning:</strong> passing a null source creates an invalid
         * {@code FilterInputStream}, that fails on every method that is not
         * overridden. Subclasses should check for null in their constructors.
         *
         * @param in the input stream to filter reads on.
         */
        constructor(__in: java.io.InputStream);
        available(): number;
        /**
         * Closes this stream. This implementation closes the filtered stream.
         *
         * @throws IOException
         * if an error occurs while closing this stream.
         */
        close(): void;
        /**
         * Sets a mark position in this stream. The parameter {@code readlimit}
         * indicates how many bytes can be read before the mark is invalidated.
         * Sending {@code reset()} will reposition this stream back to the marked
         * position, provided that {@code readlimit} has not been surpassed.
         * <p>
         * This implementation sets a mark in the filtered stream.
         *
         * @param readlimit
         * the number of bytes that can be read from this stream before
         * the mark is invalidated.
         * @see #markSupported()
         * @see #reset()
         */
        mark(readlimit: number): void;
        /**
         * Indicates whether this stream supports {@code mark()} and {@code reset()}.
         * This implementation returns whether or not the filtered stream supports
         * marking.
         *
         * @return {@code true} if {@code mark()} and {@code reset()} are supported,
         * {@code false} otherwise.
         * @see #mark(int)
         * @see #reset()
         * @see #skip(long)
         */
        markSupported(): boolean;
        /**
         * Reads a single byte from the filtered stream and returns it as an integer
         * in the range from 0 to 255. Returns -1 if the end of this stream has been
         * reached.
         *
         * @return the byte read or -1 if the end of the filtered stream has been
         * reached.
         * @throws IOException
         * if the stream is closed or another IOException occurs.
         */
        read$(): number;
        read(buffer?: any, byteOffset?: any, byteCount?: any): any;
        /**
         * Resets this stream to the last marked location. This implementation
         * resets the target stream.
         *
         * @throws IOException
         * if this stream is already closed, no mark has been set or the
         * mark is no longer valid because more than {@code readlimit}
         * bytes have been read since setting the mark.
         * @see #mark(int)
         * @see #markSupported()
         */
        reset(): void;
        /**
         * Skips {@code byteCount} bytes in this stream. Subsequent
         * calls to {@code read} will not return these bytes unless {@code reset} is
         * used. This implementation skips {@code byteCount} bytes in the
         * filtered stream.
         *
         * @return the number of bytes actually skipped.
         * @throws IOException
         * if this stream is closed or another IOException occurs.
         * @see #mark(int)
         * @see #reset()
         */
        skip(byteCount: number): number;
    }
}
declare namespace java.io {
    /**
     * A specialized {@link OutputStream} for class for writing content to an
     * (internal) byte array. As bytes are written to this stream, the byte array
     * may be expanded to hold more bytes. When the writing is considered to be
     * finished, a copy of the byte array can be requested from the class.
     *
     * @see ByteArrayInputStream
     */
    class ByteArrayOutputStream extends java.io.OutputStream {
        /**
         * The byte array containing the bytes written.
         */
        buf: number[];
        /**
         * The number of bytes written.
         */
        count: number;
        /**
         * Constructs a new {@code ByteArrayOutputStream} with a default size of
         * {@code size} bytes. If more than {@code size} bytes are written to this
         * instance, the underlying byte array will expand.
         *
         * @param size
         * initial size for the underlying byte array, must be
         * non-negative.
         * @throws IllegalArgumentException
         * if {@code size} < 0.
         */
        constructor(size?: any);
        /**
         * Closes this stream. This releases system resources used for this stream.
         *
         * @throws IOException
         * if an error occurs while attempting to close this stream.
         */
        close(): void;
        private expand(i);
        /**
         * Resets this stream to the beginning of the underlying byte array. All
         * subsequent writes will overwrite any bytes previously stored in this
         * stream.
         */
        reset(): void;
        /**
         * Returns the total number of bytes written to this stream so far.
         *
         * @return the number of bytes written to this stream.
         */
        size(): number;
        /**
         * Returns the contents of this ByteArrayOutputStream as a byte array. Any
         * changes made to the receiver after returning will not be reflected in the
         * byte array returned to the caller.
         *
         * @return this stream's current contents as a byte array.
         */
        toByteArray(): number[];
        /**
         * Returns the contents of this ByteArrayOutputStream as a string. Any
         * changes made to the receiver after returning will not be reflected in the
         * string returned to the caller.
         *
         * @return this stream's current contents as a string.
         */
        toString$(): string;
        /**
         * Returns the contents of this ByteArrayOutputStream as a string. Each byte
         * {@code b} in this stream is converted to a character {@code c} using the
         * following function:
         * {@code c == (char)(((hibyte & 0xff) << 8) | (b & 0xff))}. This method is
         * deprecated and either {@link #toString()} or {@link #toString(String)}
         * should be used.
         *
         * @param hibyte
         * the high byte of each resulting Unicode character.
         * @return this stream's current contents as a string with the high byte set
         * to {@code hibyte}.
         * @deprecated Use {@link #toString()} instead.
         */
        toString$int(hibyte: number): string;
        /**
         * Returns the contents of this ByteArrayOutputStream as a string converted
         * according to the encoding declared in {@code charsetName}.
         *
         * @param charsetName
         * a string representing the encoding to use when translating
         * this stream to a string.
         * @return this stream's current contents as an encoded string.
         * @throws UnsupportedEncodingException
         * if the provided encoding is not supported.
         */
        toString(charsetName?: any): any;
        /**
         * Writes {@code count} bytes from the byte array {@code buffer} starting at
         * offset {@code index} to this stream.
         *
         * @param buffer
         * the buffer to be written.
         * @param offset
         * the initial position in {@code buffer} to retrieve bytes.
         * @param len
         * the number of bytes of {@code buffer} to write.
         * @throws NullPointerException
         * if {@code buffer} is {@code null}.
         * @throws IndexOutOfBoundsException
         * if {@code offset < 0} or {@code len < 0}, or if
         * {@code offset + len} is greater than the length of
         * {@code buffer}.
         */
        write(buffer?: any, offset?: any, len?: any): any;
        /**
         * Writes the specified byte {@code oneByte} to the OutputStream. Only the
         * low order byte of {@code oneByte} is written.
         *
         * @param oneByte
         * the byte to be written.
         */
        write$int(oneByte: number): void;
        /**
         * Takes the contents of this stream and writes it to the output stream
         * {@code out}.
         *
         * @param out
         * an OutputStream on which to write the contents of this stream.
         * @throws IOException
         * if an error occurs while writing to {@code out}.
         */
        writeTo(out: java.io.OutputStream): void;
    }
}
declare namespace java.io {
    /**
     * JSweet partial implementation based on a local storage FS.
     */
    class FileOutputStream extends java.io.OutputStream {
        /**
         * True if the file is opened for append.
         */
        private append;
        private file;
        private entry;
        private content;
        constructor(name?: any, append?: any);
        private write$int$boolean(b, append);
        write$int(b: number): void;
        private writeBytes(b, off, len, append);
        write$byte_A(b: number[]): void;
        write(b?: any, off?: any, len?: any): any;
        flush(): void;
        close(): void;
    }
}
declare namespace java.io {
    /**
     * Wraps an existing {@link OutputStream} and performs some transformation on
     * the output data while it is being written. Transformations can be anything
     * from a simple byte-wise filtering output data to an on-the-fly compression or
     * decompression of the underlying stream. Output streams that wrap another
     * output stream and provide some additional functionality on top of it usually
     * inherit from this class.
     *
     * @see FilterOutputStream
     */
    class FilterOutputStream extends java.io.OutputStream {
        /**
         * The target output stream for this filter stream.
         */
        out: java.io.OutputStream;
        /**
         * Constructs a new {@code FilterOutputStream} with {@code out} as its
         * target stream.
         *
         * @param out
         * the target stream that this stream writes to.
         */
        constructor(out: java.io.OutputStream);
        /**
         * Closes this stream. This implementation closes the target stream.
         *
         * @throws IOException
         * if an error occurs attempting to close this stream.
         */
        close(): void;
        /**
         * Ensures that all pending data is sent out to the target stream. This
         * implementation flushes the target stream.
         *
         * @throws IOException
         * if an error occurs attempting to flush this stream.
         */
        flush(): void;
        /**
         * Writes {@code count} bytes from the byte array {@code buffer} starting at
         * {@code offset} to the target stream.
         *
         * @param buffer
         * the buffer to write.
         * @param offset
         * the index of the first byte in {@code buffer} to write.
         * @param length
         * the number of bytes in {@code buffer} to write.
         * @throws IndexOutOfBoundsException
         * if {@code offset < 0} or {@code count < 0}, or if
         * {@code offset + count} is bigger than the length of
         * {@code buffer}.
         * @throws IOException
         * if an I/O error occurs while writing to this stream.
         */
        write(buffer?: any, offset?: any, length?: any): any;
        /**
         * Writes one byte to the target stream. Only the low order byte of the
         * integer {@code oneByte} is written.
         *
         * @param oneByte
         * the byte to be written.
         * @throws IOException
         * if an I/O error occurs while writing to this stream.
         */
        write$int(oneByte: number): void;
    }
}
declare namespace java.io {
    /**
     * JSweet implementation.
     */
    class BufferedReader extends java.io.Reader {
        private in;
        private cb;
        private nChars;
        private nextChar;
        static INVALIDATED: number;
        static UNMARKED: number;
        private markedChar;
        private readAheadLimit;
        /**
         * If the next character is a line feed, skip it
         */
        private skipLF;
        /**
         * The skipLF flag when the mark was set
         */
        private markedSkipLF;
        static defaultCharBufferSize: number;
        static defaultExpectedLineLength: number;
        constructor(__in?: any, sz?: any);
        /**
         * Checks to make sure that the stream has not been closed
         */
        private ensureOpen();
        /**
         * Fills the input buffer, taking the mark into account if it is valid.
         */
        private fill();
        read$(): number;
        private read1(cbuf, off, len);
        read(cbuf?: any, off?: any, len?: any): any;
        readLine(ignoreLF?: boolean): string;
        skip(n: number): number;
        ready(): boolean;
        markSupported(): boolean;
        mark(readAheadLimit: number): void;
        reset(): void;
        close(): void;
    }
}
declare namespace java.io {
    /**
     * JSweet implementation.
     */
    class InputStreamReader extends java.io.Reader {
        in: java.io.InputStream;
        constructor(__in?: any, charsetName?: any);
        read(cbuf?: any, offset?: any, length?: any): any;
        ready(): boolean;
        close(): void;
    }
}
declare namespace java.io {
    /**
     * JSweet implementation (partial).
     *
     * TODO: actual support of charsets.
     */
    class OutputStreamWriter extends java.io.Writer {
        private out;
        constructor(out?: any, charsetName?: any);
        flushBuffer(): void;
        write$int(c: number): void;
        write(cbuf?: any, off?: any, len?: any): any;
        write$java_lang_String$int$int(str: string, off: number, len: number): void;
        flush(): void;
        close(): void;
    }
}
declare namespace java.lang {
    /**
     * A fast way to create strings using multiple appends.
     *
     * This class is an exact clone of {@link StringBuilder} except for the name.
     * Any change made to one should be mirrored in the other.
     */
    class StringBuffer extends java.lang.AbstractStringBuilder implements java.lang.CharSequence, java.lang.Appendable {
        constructor(s?: any);
        append$boolean(x: boolean): java.lang.StringBuffer;
        append$char(x: string): java.lang.StringBuffer;
        append$char_A(x: string[]): java.lang.StringBuffer;
        append(x?: any, start?: any, len?: any): any;
        append$java_lang_CharSequence(x: string): java.lang.StringBuffer;
        append$java_lang_CharSequence$int$int(x: string, start: number, end: number): java.lang.StringBuffer;
        append$double(x: number): java.lang.StringBuffer;
        append$float(x: number): java.lang.StringBuffer;
        append$int(x: number): java.lang.StringBuffer;
        append$long(x: number): java.lang.StringBuffer;
        append$java_lang_Object(x: any): java.lang.StringBuffer;
        append$java_lang_String(x: string): java.lang.StringBuffer;
        append$java_lang_StringBuffer(x: java.lang.StringBuffer): java.lang.StringBuffer;
        appendCodePoint(x: number): java.lang.StringBuffer;
        delete(start: number, end: number): java.lang.StringBuffer;
        deleteCharAt(start: number): java.lang.StringBuffer;
        insert$int$boolean(index: number, x: boolean): java.lang.StringBuffer;
        insert$int$char(index: number, x: string): java.lang.StringBuffer;
        insert$int$char_A(index: number, x: string[]): java.lang.StringBuffer;
        insert(index?: any, x?: any, offset?: any, len?: any): any;
        insert$int$java_lang_CharSequence(index: number, chars: string): java.lang.StringBuffer;
        insert$int$java_lang_CharSequence$int$int(index: number, chars: string, start: number, end: number): java.lang.StringBuffer;
        insert$int$double(index: number, x: number): java.lang.StringBuffer;
        insert$int$float(index: number, x: number): java.lang.StringBuffer;
        insert$int$int(index: number, x: number): java.lang.StringBuffer;
        insert$int$long(index: number, x: number): java.lang.StringBuffer;
        insert$int$java_lang_Object(index: number, x: any): java.lang.StringBuffer;
        insert$int$java_lang_String(index: number, x: string): java.lang.StringBuffer;
        replace(start: number, end: number, toInsert: string): java.lang.StringBuffer;
        reverse(): java.lang.StringBuffer;
    }
}
declare namespace java.lang {
    /**
     * A fast way to create strings using multiple appends.
     *
     * This class is an exact clone of {@link StringBuffer} except for the name. Any
     * change made to one should be mirrored in the other.
     */
    class StringBuilder extends java.lang.AbstractStringBuilder implements java.lang.CharSequence, java.lang.Appendable {
        constructor(s?: any);
        append$boolean(x: boolean): java.lang.StringBuilder;
        append$char(x: string): java.lang.StringBuilder;
        append$char_A(x: string[]): java.lang.StringBuilder;
        append(x?: any, start?: any, len?: any): any;
        append$java_lang_CharSequence(x: string): java.lang.StringBuilder;
        append$java_lang_CharSequence$int$int(x: string, start: number, end: number): java.lang.StringBuilder;
        append$double(x: number): java.lang.StringBuilder;
        append$float(x: number): java.lang.StringBuilder;
        append$int(x: number): java.lang.StringBuilder;
        append$long(x: number): java.lang.StringBuilder;
        append$java_lang_Object(x: any): java.lang.StringBuilder;
        append$java_lang_String(x: string): java.lang.StringBuilder;
        append$java_lang_StringBuffer(x: java.lang.StringBuffer): java.lang.StringBuilder;
        appendCodePoint(x: number): java.lang.StringBuilder;
        delete(start: number, end: number): java.lang.StringBuilder;
        deleteCharAt(start: number): java.lang.StringBuilder;
        insert$int$boolean(index: number, x: boolean): java.lang.StringBuilder;
        insert$int$char(index: number, x: string): java.lang.StringBuilder;
        insert$int$char_A(index: number, x: string[]): java.lang.StringBuilder;
        insert(index?: any, x?: any, offset?: any, len?: any): any;
        insert$int$java_lang_CharSequence(index: number, chars: string): java.lang.StringBuilder;
        insert$int$java_lang_CharSequence$int$int(index: number, chars: string, start: number, end: number): java.lang.StringBuilder;
        insert$int$double(index: number, x: number): java.lang.StringBuilder;
        insert$int$float(index: number, x: number): java.lang.StringBuilder;
        insert$int$int(index: number, x: number): java.lang.StringBuilder;
        insert$int$long(index: number, x: number): java.lang.StringBuilder;
        insert$int$java_lang_Object(index: number, x: any): java.lang.StringBuilder;
        insert$int$java_lang_String(index: number, x: string): java.lang.StringBuilder;
        replace(start: number, end: number, toInsert: string): java.lang.StringBuilder;
        reverse(): java.lang.StringBuilder;
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>NoninvertibleTransformException</code> class represents an
     * exception that is thrown if an operation is performed requiring the inverse
     * of an {@link AffineTransform} object but the <code>AffineTransform</code> is
     * in a non-invertible state.
     */
    class NoninvertibleTransformException extends java.lang.Exception {
        /**
         * Constructs an instance of <code>NoninvertibleTransformException</code>
         * with the specified detail message.
         *
         * @param s
         * the detail message
         * @since 1.2
         */
        constructor(s: string);
    }
}
declare namespace java.io {
    /**
     * See <a
     * href="http://java.sun.com/javase/6/docs/api/java/io/IOException.html">the
     * official Java API doc</a> for details.
     */
    class IOException extends Error {
        constructor(message?: any, throwable?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/CloneNotSupportedException.html">
     * the official Java API doc</a> for details.
     */
    class CloneNotSupportedException extends Error {
        constructor(msg?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/NoSuchMethodException.html">the
     * official Java API doc</a> for details.
     *
     * This exception is never thrown by GWT or GWT's libraries, as GWT does not support reflection. It
     * is provided in GWT only for compatibility with user code that explicitly throws or catches it for
     * non-reflection purposes.
     */
    class NoSuchMethodException extends Error {
        constructor(message?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/RuntimeException.html">the
     * official Java API doc</a> for details.
     */
    class RuntimeException extends Error {
        constructor(message?: any, cause?: any, enableSuppression?: any, writableStackTrace?: any);
    }
}
declare namespace java.security {
    /**
     * A generic security exception type - <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/security/GeneralSecurityException.html">[Sun's
     * docs]</a>.
     */
    class GeneralSecurityException extends Error {
        constructor(msg?: any);
    }
}
declare namespace java.text {
    /**
     * Emulation of {@code java.text.ParseException}.
     */
    class ParseException extends Error {
        private errorOffset;
        constructor(s: string, errorOffset: number);
        getErrorOffset(): number;
    }
}
declare namespace java.util {
    /**
     * Thrown when the subject of an observer cannot support additional observers.
     *
     */
    class TooManyListenersException extends Error {
        constructor(message?: any);
    }
}
declare namespace java.lang {
    /**
     * Thrown to indicate some unexpected internal error has occurred in
     * the Java Virtual Machine.
     *
     * @author  unascribed
     * @since   JDK1.0
     */
    class InternalError extends java.lang.VirtualMachineError {
        static serialVersionUID: number;
        /**
         * Constructs an {@code InternalError} with the specified detail
         * message and cause.  <p>Note that the detail message associated
         * with {@code cause} is <i>not</i> automatically incorporated in
         * this error's detail message.
         *
         * @param  message the detail message (which is saved for later retrieval
         * by the {@link #getMessage()} method).
         * @param  cause the cause (which is saved for later retrieval by the
         * {@link #getCause()} method).  (A {@code null} value is
         * permitted, and indicates that the cause is nonexistent or
         * unknown.)
         * @since  1.8
         */
        constructor(message?: any, cause?: any);
    }
}
declare namespace javaemul.internal {
    /**
     * Provides Charset implementations.
     */
    abstract class EmulatedCharset extends java.nio.charset.Charset {
        static UTF_8: EmulatedCharset;
        static UTF_8_$LI$(): EmulatedCharset;
        static ISO_LATIN_1: EmulatedCharset;
        static ISO_LATIN_1_$LI$(): EmulatedCharset;
        static ISO_8859_1: EmulatedCharset;
        static ISO_8859_1_$LI$(): EmulatedCharset;
        constructor(name: string);
        abstract getBytes(string: string): number[];
        abstract decodeString(bytes: number[], ofs: number, len: number): string[];
    }
    namespace EmulatedCharset {
        class LatinCharset extends javaemul.internal.EmulatedCharset {
            constructor(name: string);
            getBytes(str: string): number[];
            decodeString(bytes: number[], ofs: number, len: number): string[];
        }
        class UtfCharset extends javaemul.internal.EmulatedCharset {
            constructor(name: string);
            decodeString(bytes: number[], ofs: number, len: number): string[];
            getBytes(str: string): number[];
            /**
             * Encode a single character in UTF8.
             *
             * @param bytes byte array to store character in
             * @param ofs offset into byte array to store first byte
             * @param codePoint character to encode
             * @return number of bytes consumed by encoding the character
             * @throws IllegalArgumentException if codepoint >= 2^26
             */
            encodeUtf8(bytes: number[], ofs: number, codePoint: number): number;
        }
    }
}
declare namespace java.security {
    /**
     * Message Digest algorithm - <a href=
     * "http://java.sun.com/j2se/1.4.2/docs/api/java/security/MessageDigest.html"
     * >[Sun's docs]</a>.
     */
    abstract class MessageDigest extends java.security.MessageDigestSpi {
        static getInstance(algorithm: string): MessageDigest;
        static isEqual(digestA: number[], digestB: number[]): boolean;
        private algorithm;
        constructor(algorithm: string);
        digest$(): number[];
        digest$byte_A(input: number[]): number[];
        digest(buf?: any, offset?: any, len?: any): any;
        getAlgorithm(): string;
        getDigestLength(): number;
        reset(): void;
        update$byte(input: number): void;
        update$byte_A(input: number[]): void;
        update(input?: any, offset?: any, len?: any): any;
    }
    namespace MessageDigest {
        class Md5Digest extends java.security.MessageDigest {
            static padding: number[];
            static padding_$LI$(): number[];
            /**
             * Converts a long to a 8-byte array using low order first.
             *
             * @param n A long.
             * @return A byte[].
             */
            static toBytes(n: number): number[];
            /**
             * Converts a 64-byte array into a 16-int array.
             *
             * @param in A byte[].
             * @param out An int[].
             */
            static byte2int(__in: number[], out: number[]): void;
            static f(x: number, y: number, z: number): number;
            static ff(a: number, b: number, c: number, d: number, x: number, s: number, ac: number): number;
            static g(x: number, y: number, z: number): number;
            static gg(a: number, b: number, c: number, d: number, x: number, s: number, ac: number): number;
            static h(x: number, y: number, z: number): number;
            static hh(a: number, b: number, c: number, d: number, x: number, s: number, ac: number): number;
            static i(x: number, y: number, z: number): number;
            static ii(a: number, b: number, c: number, d: number, x: number, s: number, ac: number): number;
            /**
             * Converts a 4-int array into a 16-byte array.
             *
             * @param in An int[].
             * @param out A byte[].
             */
            static int2byte(__in: number[], out: number[]): void;
            buffer: number[];
            counter: number;
            oneByte: number[];
            remainder: number;
            state: number[];
            x: number[];
            constructor();
            engineDigest$(): number[];
            engineGetDigestLength(): number;
            engineReset(): void;
            engineUpdate$byte(input: number): void;
            engineUpdate(input?: any, offset?: any, len?: any): any;
            transform(buffer: number[]): void;
        }
    }
}
declare namespace java.util {
    /**
     * Skeletal implementation of the List interface. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/AbstractList.html">[Sun
     * docs]</a>
     *
     * @param <E> the element type.
     */
    abstract class AbstractList<E> extends java.util.AbstractCollection<E> implements java.util.List<E> {
        forEach(action: (p1: any) => void): void;
        abstract size(): any;
        modCount: number;
        constructor();
        add$java_lang_Object(obj: E): boolean;
        add(index?: any, element?: any): any;
        addAll(index?: any, c?: any): any;
        clear(): void;
        equals(o: any): boolean;
        abstract get(index: number): E;
        hashCode(): number;
        indexOf(o?: any, index?: any): any;
        indexOf$java_lang_Object(toFind: any): number;
        iterator(): java.util.Iterator<E>;
        lastIndexOf(o?: any, index?: any): any;
        lastIndexOf$java_lang_Object(toFind: any): number;
        listIterator$(): java.util.ListIterator<E>;
        listIterator(from?: any): any;
        remove(index?: any): any;
        set(index: number, o: E): E;
        subList(fromIndex: number, toIndex: number): java.util.List<E>;
        removeRange(fromIndex: number, endIndex: number): void;
    }
    namespace AbstractList {
        class IteratorImpl implements java.util.Iterator<any> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            i: number;
            last: number;
            constructor(__parent: any);
            hasNext(): boolean;
            next(): any;
            remove(): void;
        }
        /**
         * Implementation of <code>ListIterator</code> for abstract lists.
         */
        class ListIteratorImpl extends AbstractList.IteratorImpl implements java.util.ListIterator<any> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            constructor(__parent: any, start?: any);
            add(o: any): void;
            hasPrevious(): boolean;
            nextIndex(): number;
            previous(): any;
            previousIndex(): number;
            set(o: any): void;
        }
        class SubList<E> extends java.util.AbstractList<E> {
            wrapped: java.util.List<E>;
            fromIndex: number;
            __size: number;
            constructor(wrapped: java.util.List<E>, fromIndex: number, toIndex: number);
            add(index?: any, element?: any): any;
            get(index: number): E;
            remove(index?: any): any;
            set(index: number, element: E): E;
            size(): number;
        }
    }
}
declare namespace java.util {
    /**
     * Skeletal implementation of the Queue interface. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/AbstractQueue.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    abstract class AbstractQueue<E> extends java.util.AbstractCollection<E> implements java.util.Queue<E> {
        forEach(action: (p1: any) => void): void;
        abstract iterator(): any;
        abstract size(): any;
        constructor();
        add(index?: any, element?: any): any;
        add$java_lang_Object(o: E): boolean;
        addAll(index?: any, c?: any): any;
        addAll$java_util_Collection(c: java.util.Collection<any>): boolean;
        clear(): void;
        element(): E;
        abstract offer(o: E): boolean;
        abstract peek(): E;
        abstract poll(): E;
        remove(index?: any): any;
        remove$(): E;
    }
}
declare namespace java.util {
    /**
     * Skeletal implementation of the Set interface. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/AbstractSet.html">[Sun
     * docs]</a>
     *
     * @param <E> the element type.
     */
    abstract class AbstractSet<E> extends java.util.AbstractCollection<E> implements java.util.Set<E> {
        forEach(action: (p1: any) => void): void;
        abstract iterator(): any;
        abstract size(): any;
        equals(o: any): boolean;
        hashCode(): number;
        removeAll(c: java.util.Collection<any>): boolean;
        constructor();
    }
}
declare namespace java.util {
    /**
     * A simple wrapper around JavaScript Map for key type is string.
     */
    class InternalStringMap<K, V> implements java.lang.Iterable<Map.Entry<K, V>> {
        forEach(action: (p1: any) => void): void;
        private backingMap;
        private host;
        private size;
        /**
         * A mod count to track 'value' replacements in map to ensure that the
         * 'value' that we have in the iterator entry is guaranteed to be still
         * correct. This is to optimize for the common scenario where the values are
         * not modified during iterations where the entries are never stale.
         */
        private valueMod;
        constructor(host: java.util.AbstractHashMap<K, V>);
        contains(key: string): boolean;
        get(key: string): V;
        put(key: string, value: V): V;
        remove(key: string): V;
        getSize(): number;
        iterator(): java.util.Iterator<Map.Entry<K, V>>;
        private newMapEntry(entry, lastValueMod);
        private static toNullIfUndefined<T>(value);
    }
    namespace InternalStringMap {
        class InternalStringMap$0 implements java.util.Iterator<java.util.Map.Entry<any, any>> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            entries: java.util.InternalJsMap.Iterator<any>;
            current: java.util.InternalJsMap.IteratorEntry<any>;
            last: java.util.InternalJsMap.IteratorEntry<any>;
            hasNext(): boolean;
            next(): Map.Entry<any, any>;
            remove(): void;
            constructor(__parent: any);
        }
        class InternalStringMap$1 extends java.util.AbstractMapEntry<any, any> {
            private entry;
            private lastValueMod;
            __parent: any;
            getKey(): any;
            getValue(): any;
            setValue(object: any): any;
            constructor(__parent: any, entry: any, lastValueMod: any);
        }
    }
}
declare namespace javaemul.internal {
    /**
     * Intrinsic string class.
     */
    class StringHelper {
        static CASE_INSENSITIVE_ORDER: java.util.Comparator<string>;
        static CASE_INSENSITIVE_ORDER_$LI$(): java.util.Comparator<string>;
        static copyValueOf$char_A(v: string[]): string;
        static copyValueOf(v?: any, offset?: any, count?: any): any;
        static valueOf$boolean(x: boolean): string;
        static valueOf$char(x: string): string;
        static valueOf(x?: any, offset?: any, count?: any): any;
        private static fromCharCode(array);
        static valueOf$char_A(x: string[]): string;
        static valueOf$double(x: number): string;
        static valueOf$float(x: number): string;
        static valueOf$int(x: number): string;
        static valueOf$long(x: number): string;
        static valueOf$java_lang_Object(x: any): string;
        /**
         * This method converts Java-escaped dollar signs "\$" into
         * JavaScript-escaped dollar signs "$$", and removes all other lone
         * backslashes, which serve as escapes in Java but are passed through
         * literally in JavaScript.
         *
         * @skip
         */
        private static translateReplaceString(replaceStr);
        private static compareTo(thisStr, otherStr);
        private static getCharset(charsetName);
        static fromCodePoint(codePoint: number): string;
        static format(formatString: string, ...args: any[]): string;
        constructor();
    }
    namespace StringHelper {
        class StringHelper$0 implements java.util.Comparator<string> {
            compare(a: string, b: string): number;
            constructor();
        }
    }
}
declare namespace java.sql {
    /**
     * An implementation of java.sql.Date. Derived from
     * http://java.sun.com/j2se/1.5.0/docs/api/java/sql/Date.html
     */
    class Date extends java.util.Date {
        static valueOf(s: string): Date;
        constructor(year?: any, month?: any, day?: any);
        getHours(): number;
        getMinutes(): number;
        getSeconds(): number;
        setHours(i: number): void;
        setMinutes(i: number): void;
        setSeconds(i: number): void;
    }
}
declare namespace java.sql {
    /**
     * An implementation of java.sql.Time. Derived from
     * http://java.sun.com/j2se/1.5.0/docs/api/java/sql/Time.html
     */
    class Time extends java.util.Date {
        static valueOf(s: string): Time;
        constructor(hour?: any, minute?: any, second?: any);
        getDate(): number;
        getDay(): number;
        getMonth(): number;
        getYear(): number;
        setDate(i: number): void;
        setMonth(i: number): void;
        setYear(i: number): void;
    }
}
declare namespace java.sql {
    /**
     * An implementation of java.sql.Timestame. Derived from
     * http://java.sun.com/j2se/1.5.0/docs/api/java/sql/Timestamp.html. This is
     * basically just regular Date decorated with a nanoseconds field.
     */
    class Timestamp extends java.util.Date {
        static valueOf(s: string): Timestamp;
        private static padNine(value);
        /**
         * Stores the nanosecond resolution of the timestamp; must be kept in sync
         * with the sub-second part of Date.millis.
         */
        private nanos;
        constructor(year?: any, month?: any, date?: any, hour?: any, minute?: any, second?: any, nano?: any);
        after(ts?: any): any;
        before(ts?: any): any;
        compareTo$java_util_Date(o: java.util.Date): number;
        compareTo(o?: any): any;
        equals$java_lang_Object(ts: any): boolean;
        equals(ts?: any): any;
        getNanos(): number;
        getTime(): number;
        hashCode(): number;
        setNanos(n: number): void;
        setTime(time: number): void;
    }
}
declare namespace java.util.logging {
    /**
     * A simple console logger used in super dev mode.
     */
    class SimpleConsoleLogHandler extends java.util.logging.Handler {
        publish(record: java.util.logging.LogRecord): void;
        private toConsoleLogLevel(level);
        close(): void;
        flush(): void;
    }
}
declare namespace javaemul.internal {
    /**
     * Wraps native <code>byte</code> as an object.
     */
    class ByteHelper extends javaemul.internal.NumberHelper implements java.lang.Comparable<ByteHelper> {
        static MIN_VALUE: number;
        static MIN_VALUE_$LI$(): number;
        static MAX_VALUE: number;
        static MAX_VALUE_$LI$(): number;
        static SIZE: number;
        static TYPE: typeof Number;
        static TYPE_$LI$(): typeof Number;
        static compare(x: number, y: number): number;
        static decode(s: string): ByteHelper;
        /**
         * @skip
         *
         * Here for shared implementation with Arrays.hashCode
         */
        static hashCode(b: number): number;
        static parseByte(s: string, radix?: number): number;
        static toString(b: number): string;
        static valueOf$byte(b: number): ByteHelper;
        static valueOf$java_lang_String(s: string): ByteHelper;
        static valueOf(s?: any, radix?: any): any;
        private value;
        constructor(s?: any);
        byteValue(): number;
        compareTo(b?: any): any;
        doubleValue(): number;
        equals(o: any): boolean;
        floatValue(): number;
        hashCode(): number;
        intValue(): number;
        longValue(): number;
        shortValue(): number;
        toString(): string;
    }
    namespace ByteHelper {
        /**
         * Use nested class to avoid clinit on outer.
         */
        class BoxedValues {
            static boxedValues: javaemul.internal.ByteHelper[];
            static boxedValues_$LI$(): javaemul.internal.ByteHelper[];
        }
    }
}
declare namespace javaemul.internal {
    /**
     * Wraps a primitive <code>double</code> as an object.
     */
    class DoubleHelper extends javaemul.internal.NumberHelper implements java.lang.Comparable<DoubleHelper> {
        static MAX_VALUE: number;
        static MIN_VALUE: number;
        static MIN_NORMAL: number;
        static MAX_EXPONENT: number;
        static MIN_EXPONENT: number;
        static NaN: number;
        static NaN_$LI$(): number;
        static NEGATIVE_INFINITY: number;
        static NEGATIVE_INFINITY_$LI$(): number;
        static POSITIVE_INFINITY: number;
        static POSITIVE_INFINITY_$LI$(): number;
        static SIZE: number;
        static POWER_512: number;
        static POWER_MINUS_512: number;
        static POWER_256: number;
        static POWER_MINUS_256: number;
        static POWER_128: number;
        static POWER_MINUS_128: number;
        static POWER_64: number;
        static POWER_MINUS_64: number;
        static POWER_52: number;
        static POWER_MINUS_52: number;
        static POWER_32: number;
        static POWER_MINUS_32: number;
        static POWER_31: number;
        static POWER_20: number;
        static POWER_MINUS_20: number;
        static POWER_16: number;
        static POWER_MINUS_16: number;
        static POWER_8: number;
        static POWER_MINUS_8: number;
        static POWER_4: number;
        static POWER_MINUS_4: number;
        static POWER_2: number;
        static POWER_MINUS_2: number;
        static POWER_1: number;
        static POWER_MINUS_1: number;
        static POWER_MINUS_1022: number;
        static compare(x: number, y: number): number;
        static doubleToLongBits(value: number): number;
        /**
         * @skip Here for shared implementation with Arrays.hashCode
         */
        static hashCode(d: number): number;
        static isInfinite(x: number): boolean;
        static isNaN(x: number): boolean;
        static longBitsToDouble(bits: number): number;
        static parseDouble(s: string): number;
        static toString(b: number): string;
        static valueOf$double(d: number): DoubleHelper;
        static valueOf(s?: any): any;
        constructor(s?: any);
        byteValue(): number;
        compareTo(b?: any): any;
        doubleValue(): number;
        static unsafeCast(instance: any): number;
        equals(o: any): boolean;
        floatValue(): number;
        /**
         * Performance caution: using Double objects as map keys is not recommended.
         * Using double values as keys is generally a bad idea due to difficulty
         * determining exact equality. In addition, there is no efficient JavaScript
         * equivalent of <code>doubleToIntBits</code>. As a result, this method
         * computes a hash code by truncating the whole number portion of the
         * double, which may lead to poor performance for certain value sets if
         * Doubles are used as keys in a {@link java.util.HashMap}.
         */
        hashCode(): number;
        intValue(): number;
        isInfinite(): boolean;
        isNaN(): boolean;
        longValue(): number;
        shortValue(): number;
        toString(): string;
    }
    namespace DoubleHelper {
        class PowersTable {
            static powers: number[];
            static powers_$LI$(): number[];
            static invPowers: number[];
            static invPowers_$LI$(): number[];
        }
    }
}
declare namespace javaemul.internal {
    /**
     * Wraps a primitive <code>float</code> as an object.
     */
    class FloatHelper extends javaemul.internal.NumberHelper implements java.lang.Comparable<FloatHelper> {
        static MAX_VALUE: number;
        static MIN_VALUE: number;
        static MAX_EXPONENT: number;
        static MIN_EXPONENT: number;
        static MIN_NORMAL: number;
        static NaN: number;
        static NaN_$LI$(): number;
        static NEGATIVE_INFINITY: number;
        static NEGATIVE_INFINITY_$LI$(): number;
        static POSITIVE_INFINITY: number;
        static POSITIVE_INFINITY_$LI$(): number;
        static SIZE: number;
        static POWER_31_INT: number;
        static compare(x: number, y: number): number;
        static floatToIntBits(value: number): number;
        /**
         * @skip Here for shared implementation with Arrays.hashCode.
         * @param f
         * @return hash value of float (currently just truncated to int)
         */
        static hashCode(f: number): number;
        static intBitsToFloat(bits: number): number;
        static isInfinite(x: number): boolean;
        static isNaN(x: number): boolean;
        static parseFloat(s: string): number;
        static toString(b: number): string;
        static valueOf$float(f: number): FloatHelper;
        static valueOf(s?: any): any;
        private value;
        constructor(s?: any);
        byteValue(): number;
        compareTo(b?: any): any;
        doubleValue(): number;
        equals(o: any): boolean;
        floatValue(): number;
        /**
         * Performance caution: using Float objects as map keys is not recommended.
         * Using floating point values as keys is generally a bad idea due to
         * difficulty determining exact equality. In addition, there is no efficient
         * JavaScript equivalent of <code>floatToIntBits</code>. As a result, this
         * method computes a hash code by truncating the whole number portion of the
         * float, which may lead to poor performance for certain value sets if
         * Floats are used as keys in a {@link java.util.HashMap}.
         */
        hashCode(): number;
        intValue(): number;
        isInfinite(): boolean;
        isNaN(): boolean;
        longValue(): number;
        shortValue(): number;
        toString(): string;
    }
}
declare namespace javaemul.internal {
    /**
     * Wraps a primitive <code>int</code> as an object.
     */
    class IntegerHelper extends javaemul.internal.NumberHelper implements java.lang.Comparable<IntegerHelper> {
        static MAX_VALUE: number;
        static MIN_VALUE: number;
        static SIZE: number;
        static bitCount(x: number): number;
        static compare(x: number, y: number): number;
        static decode(s: string): IntegerHelper;
        /**
         * @skip
         *
         * Here for shared implementation with Arrays.hashCode
         */
        static hashCode(i: number): number;
        static highestOneBit(i: number): number;
        static lowestOneBit(i: number): number;
        static numberOfLeadingZeros(i: number): number;
        static numberOfTrailingZeros(i: number): number;
        static parseInt(s: string, radix?: number): number;
        static reverse(i: number): number;
        static reverseBytes(i: number): number;
        static rotateLeft(i: number, distance: number): number;
        static rotateRight(i: number, distance: number): number;
        static signum(i: number): number;
        static toBinaryString(value: number): string;
        static toHexString(value: number): string;
        static toOctalString(value: number): string;
        static toString$int(value: number): string;
        static toString(value?: any, radix?: any): any;
        static valueOf$int(i: number): IntegerHelper;
        static valueOf$java_lang_String(s: string): IntegerHelper;
        static valueOf(s?: any, radix?: any): any;
        static toRadixString(value: number, radix: number): string;
        static toUnsignedRadixString(value: number, radix: number): string;
        private value;
        constructor(s?: any);
        byteValue(): number;
        compareTo(b?: any): any;
        doubleValue(): number;
        equals(o: any): boolean;
        floatValue(): number;
        hashCode(): number;
        intValue(): number;
        longValue(): number;
        shortValue(): number;
        toString(): string;
    }
    namespace IntegerHelper {
        /**
         * Use nested class to avoid clinit on outer.
         */
        class BoxedValues {
            static boxedValues: javaemul.internal.IntegerHelper[];
            static boxedValues_$LI$(): javaemul.internal.IntegerHelper[];
        }
        /**
         * Use nested class to avoid clinit on outer.
         */
        class ReverseNibbles {
            /**
             * A fast-lookup of the reversed bits of all the nibbles 0-15. Used to
             * implement {@link #reverse(int)}.
             */
            static reverseNibbles: number[];
            static reverseNibbles_$LI$(): number[];
        }
    }
}
declare namespace javaemul.internal {
    /**
     * Wraps a primitive <code>long</code> as an object.
     */
    class LongHelper extends javaemul.internal.NumberHelper implements java.lang.Comparable<LongHelper> {
        static MAX_VALUE: number;
        static MIN_VALUE: number;
        static SIZE: number;
        static bitCount(i: number): number;
        static compare(x: number, y: number): number;
        static decode(s: string): LongHelper;
        /**
         * @skip Here for shared implementation with Arrays.hashCode
         */
        static hashCode(l: number): number;
        static highestOneBit(i: number): number;
        static lowestOneBit(i: number): number;
        static numberOfLeadingZeros(i: number): number;
        static numberOfTrailingZeros(i: number): number;
        static parseLong(s: string, radix?: number): number;
        static reverse(i: number): number;
        static reverseBytes(i: number): number;
        static rotateLeft(i: number, distance: number): number;
        static rotateRight(i: number, distance: number): number;
        static signum(i: number): number;
        static toBinaryString(value: number): string;
        static toHexString(value: number): string;
        static toOctalString(value: number): string;
        static toString$long(value: number): string;
        static toString(value?: any, intRadix?: any): any;
        static valueOf$long(i: number): LongHelper;
        static valueOf$java_lang_String(s: string): LongHelper;
        static valueOf(s?: any, radix?: any): any;
        static toPowerOfTwoUnsignedString(value: number, shift: number): string;
        private value;
        constructor(s?: any);
        byteValue(): number;
        compareTo(b?: any): any;
        doubleValue(): number;
        equals(o: any): boolean;
        floatValue(): number;
        hashCode(): number;
        intValue(): number;
        longValue(): number;
        shortValue(): number;
        toString(): string;
    }
    namespace LongHelper {
        /**
         * Use nested class to avoid clinit on outer.
         */
        class BoxedValues {
            static boxedValues: javaemul.internal.LongHelper[];
            static boxedValues_$LI$(): javaemul.internal.LongHelper[];
        }
    }
}
declare namespace javaemul.internal {
    /**
     * Wraps a primitive <code>short</code> as an object.
     */
    class ShortHelper extends javaemul.internal.NumberHelper implements java.lang.Comparable<ShortHelper> {
        static MIN_VALUE: number;
        static MIN_VALUE_$LI$(): number;
        static MAX_VALUE: number;
        static MAX_VALUE_$LI$(): number;
        static SIZE: number;
        static TYPE: typeof Number;
        static TYPE_$LI$(): typeof Number;
        static compare(x: number, y: number): number;
        static decode(s: string): ShortHelper;
        /**
         * @skip Here for shared implementation with Arrays.hashCode
         */
        static hashCode(s: number): number;
        static parseShort(s: string, radix?: number): number;
        static reverseBytes(s: number): number;
        static toString(b: number): string;
        static valueOf$short(s: number): ShortHelper;
        static valueOf$java_lang_String(s: string): ShortHelper;
        static valueOf(s?: any, radix?: any): any;
        private value;
        constructor(s?: any);
        byteValue(): number;
        compareTo(b?: any): any;
        doubleValue(): number;
        equals(o: any): boolean;
        floatValue(): number;
        hashCode(): number;
        intValue(): number;
        longValue(): number;
        shortValue(): number;
        toString(): string;
    }
    namespace ShortHelper {
        /**
         * Use nested class to avoid clinit on outer.
         */
        class BoxedValues {
            static boxedValues: javaemul.internal.ShortHelper[];
            static boxedValues_$LI$(): javaemul.internal.ShortHelper[];
        }
    }
}
declare namespace sun.awt.geom {
    class Order0 extends sun.awt.geom.Curve {
        private x;
        private y;
        constructor(x: number, y: number);
        getOrder(): number;
        getXTop(): number;
        getYTop(): number;
        getXBot(): number;
        getYBot(): number;
        getXMin(): number;
        getXMax(): number;
        getX0(): number;
        getY0(): number;
        getX1(): number;
        getY1(): number;
        XforY(y: number): number;
        TforY(y: number): number;
        XforT(t: number): number;
        YforT(t: number): number;
        dXforT(t: number, deriv: number): number;
        dYforT(t: number, deriv: number): number;
        nextVertical(t0: number, t1: number): number;
        crossingsFor(x: number, y: number): number;
        accumulateCrossings(c: sun.awt.geom.Crossings): boolean;
        enlarge(r: java.awt.geom.Rectangle2D): void;
        getSubCurve(ystart?: any, yend?: any, dir?: any): any;
        getReversedCurve(): sun.awt.geom.Curve;
        getSegment(coords: number[]): number;
    }
}
declare namespace sun.awt.geom {
    class Order1 extends sun.awt.geom.Curve {
        private x0;
        private y0;
        private x1;
        private y1;
        private xmin;
        private xmax;
        constructor(x0: number, y0: number, x1: number, y1: number, direction: number);
        getOrder(): number;
        getXTop(): number;
        getYTop(): number;
        getXBot(): number;
        getYBot(): number;
        getXMin(): number;
        getXMax(): number;
        getX0(): number;
        getY0(): number;
        getX1(): number;
        getY1(): number;
        XforY(y: number): number;
        TforY(y: number): number;
        XforT(t: number): number;
        YforT(t: number): number;
        dXforT(t: number, deriv: number): number;
        dYforT(t: number, deriv: number): number;
        nextVertical(t0: number, t1: number): number;
        accumulateCrossings(c: sun.awt.geom.Crossings): boolean;
        enlarge(r: java.awt.geom.Rectangle2D): void;
        getSubCurve(ystart?: any, yend?: any, dir?: any): any;
        getReversedCurve(): sun.awt.geom.Curve;
        compareTo(other: sun.awt.geom.Curve, yrange: number[]): number;
        getSegment(coords: number[]): number;
    }
}
declare namespace sun.awt.geom {
    class Order2 extends sun.awt.geom.Curve {
        private x0;
        private y0;
        private cx0;
        private cy0;
        private x1;
        private y1;
        private xmin;
        private xmax;
        private xcoeff0;
        private xcoeff1;
        private xcoeff2;
        private ycoeff0;
        private ycoeff1;
        private ycoeff2;
        static insert(curves: java.util.Vector<any>, tmp: number[], x0: number, y0: number, cx0: number, cy0: number, x1: number, y1: number, direction: number): void;
        static addInstance(curves: java.util.Vector<any>, x0: number, y0: number, cx0: number, cy0: number, x1: number, y1: number, direction: number): void;
        static getHorizontalParams(c0: number, cp: number, c1: number, ret: number[]): number;
        static split(coords: number[], pos: number, t: number): void;
        constructor(x0: number, y0: number, cx0: number, cy0: number, x1: number, y1: number, direction: number);
        getOrder(): number;
        getXTop(): number;
        getYTop(): number;
        getXBot(): number;
        getYBot(): number;
        getXMin(): number;
        getXMax(): number;
        getX0(): number;
        getY0(): number;
        getCX0(): number;
        getCY0(): number;
        getX1(): number;
        getY1(): number;
        XforY(y: number): number;
        TforY(y: number): number;
        static TforY(y: number, ycoeff0: number, ycoeff1: number, ycoeff2: number): number;
        XforT(t: number): number;
        YforT(t: number): number;
        dXforT(t: number, deriv: number): number;
        dYforT(t: number, deriv: number): number;
        nextVertical(t0: number, t1: number): number;
        enlarge(r: java.awt.geom.Rectangle2D): void;
        getSubCurve(ystart?: any, yend?: any, dir?: any): any;
        getReversedCurve(): sun.awt.geom.Curve;
        getSegment(coords: number[]): number;
        controlPointString(): string;
    }
}
declare namespace sun.awt.geom {
    class Order3 extends sun.awt.geom.Curve {
        private x0;
        private y0;
        private cx0;
        private cy0;
        private cx1;
        private cy1;
        private x1;
        private y1;
        private xmin;
        private xmax;
        private xcoeff0;
        private xcoeff1;
        private xcoeff2;
        private xcoeff3;
        private ycoeff0;
        private ycoeff1;
        private ycoeff2;
        private ycoeff3;
        static insert(curves: java.util.Vector<any>, tmp: number[], x0: number, y0: number, cx0: number, cy0: number, cx1: number, cy1: number, x1: number, y1: number, direction: number): void;
        static addInstance(curves: java.util.Vector<any>, x0: number, y0: number, cx0: number, cy0: number, cx1: number, cy1: number, x1: number, y1: number, direction: number): void;
        static getHorizontalParams(c0: number, cp0: number, cp1: number, c1: number, ret: number[]): number;
        static split(coords: number[], pos: number, t: number): void;
        constructor(x0: number, y0: number, cx0: number, cy0: number, cx1: number, cy1: number, x1: number, y1: number, direction: number);
        getOrder(): number;
        getXTop(): number;
        getYTop(): number;
        getXBot(): number;
        getYBot(): number;
        getXMin(): number;
        getXMax(): number;
        getX0(): number;
        getY0(): number;
        getCX0(): number;
        getCY0(): number;
        getCX1(): number;
        getCY1(): number;
        getX1(): number;
        getY1(): number;
        private TforY1;
        private YforT1;
        private TforY2;
        private YforT2;
        private TforY3;
        private YforT3;
        TforY(y: number): number;
        refine(a: number, b: number, c: number, target: number, t: number): number;
        XforY(y: number): number;
        XforT(t: number): number;
        YforT(t: number): number;
        dXforT(t: number, deriv: number): number;
        dYforT(t: number, deriv: number): number;
        nextVertical(t0: number, t1: number): number;
        enlarge(r: java.awt.geom.Rectangle2D): void;
        getSubCurve(ystart?: any, yend?: any, dir?: any): any;
        getReversedCurve(): sun.awt.geom.Curve;
        getSegment(coords: number[]): number;
        controlPointString(): string;
    }
}
declare namespace sun.awt.geom {
    abstract class AreaOp {
        private verbose;
        constructor();
        static CTAG_LEFT: number;
        static CTAG_RIGHT: number;
        static ETAG_IGNORE: number;
        static ETAG_ENTER: number;
        static ETAG_EXIT: number;
        static RSTAG_INSIDE: number;
        static RSTAG_OUTSIDE: number;
        abstract newRow(): any;
        abstract classify(e: sun.awt.geom.Edge): number;
        abstract getState(): number;
        calculate(left: java.util.Vector<any>, right: java.util.Vector<any>): java.util.Vector<any>;
        static addEdges(edges: java.util.Vector<any>, curves: java.util.Vector<any>, curvetag: number): void;
        static YXTopComparator: java.util.Comparator<any>;
        static YXTopComparator_$LI$(): java.util.Comparator<any>;
        pruneEdges(edges: java.util.Vector<any>): java.util.Vector<any>;
        static finalizeSubCurves(subcurves: java.util.Vector<any>, chains: java.util.Vector<any>): void;
        static EmptyLinkList: sun.awt.geom.CurveLink[];
        static EmptyLinkList_$LI$(): sun.awt.geom.CurveLink[];
        static EmptyChainList: sun.awt.geom.ChainEnd[];
        static EmptyChainList_$LI$(): sun.awt.geom.ChainEnd[];
        static resolveLinks(subcurves: java.util.Vector<any>, chains: java.util.Vector<any>, links: java.util.Vector<any>): void;
        static obstructs(v1: number, v2: number, phase: number): boolean;
    }
    namespace AreaOp {
        abstract class CAGOp extends sun.awt.geom.AreaOp {
            inLeft: boolean;
            inRight: boolean;
            inResult: boolean;
            newRow(): void;
            classify(e: sun.awt.geom.Edge): number;
            getState(): number;
            abstract newClassification(inLeft: boolean, inRight: boolean): boolean;
            constructor();
        }
        class AddOp extends AreaOp.CAGOp {
            newClassification(inLeft: boolean, inRight: boolean): boolean;
        }
        class SubOp extends AreaOp.CAGOp {
            newClassification(inLeft: boolean, inRight: boolean): boolean;
        }
        class IntOp extends AreaOp.CAGOp {
            newClassification(inLeft: boolean, inRight: boolean): boolean;
        }
        class XorOp extends AreaOp.CAGOp {
            newClassification(inLeft: boolean, inRight: boolean): boolean;
        }
        class NZWindOp extends sun.awt.geom.AreaOp {
            count: number;
            newRow(): void;
            classify(e: sun.awt.geom.Edge): number;
            getState(): number;
            constructor();
        }
        class EOWindOp extends sun.awt.geom.AreaOp {
            inside: boolean;
            newRow(): void;
            classify(e: sun.awt.geom.Edge): number;
            getState(): number;
            constructor();
        }
        class AreaOp$0 implements java.util.Comparator<any> {
            compare(o1: any, o2: any): number;
            constructor();
        }
    }
}
declare namespace java.awt.geom {
    /**
     * The {@code GeneralPath} class represents a geometric path constructed from
     * straight lines, and quadratic and cubic (B&eacute;zier) curves. It can
     * contain multiple subpaths.
     * <p>
     * {@code GeneralPath} is a legacy final class which exactly implements the
     * behavior of its superclass {@link Path2D.Float}. Together with
     * {@link Path2D.Double}, the {@link Path2D} classes provide full
     * implementations of a general geometric path that support all of the
     * functionality of the {@link Shape} and {@link PathIterator} interfaces with
     * the ability to explicitly select different levels of internal coordinate
     * precision.
     * <p>
     * Use {@code Path2D.Float} (or this legacy {@code GeneralPath} subclass) when
     * dealing with data that can be represented and used with floating point
     * precision. Use {@code Path2D.Double} for data that requires the accuracy or
     * range of double precision.
     *
     * @author Jim Graham
     * @since 1.2
     */
    class GeneralPath extends java.awt.geom.Path2D.Float {
        constructor(windingRule?: any, pointTypes?: any, numTypes?: any, pointCoords?: any, numCoords?: any);
        static serialVersionUID: number;
    }
}
declare namespace java.awt {
    /**
     * A <code>Rectangle</code> specifies an area in a coordinate space that is
     * enclosed by the <code>Rectangle</code> object's upper-left point
     * {@code (x,y)}
     * in the coordinate space, its width, and its height.
     * <p>
     * A <code>Rectangle</code> object's <code>width</code> and
     * <code>height</code> are <code>public</code> fields. The constructors
     * that create a <code>Rectangle</code>, and the methods that can modify
     * one, do not prevent setting a negative value for width or height.
     * <p>
     * <a name="Empty">
     * A {@code Rectangle} whose width or height is exactly zero has location
     * along those axes with zero dimension, but is otherwise considered empty.
     * The {@link #isEmpty} method will return true for such a {@code Rectangle}.
     * Methods which test if an empty {@code Rectangle} contains or intersects
     * a point or rectangle will always return false if either dimension is zero.
     * Methods which combine such a {@code Rectangle} with a point or rectangle
     * will include the location of the {@code Rectangle} on that axis in the
     * result as if the {@link #add(Point)} method were being called.
     * </a>
     * <p>
     * <a name="NonExistant">
     * A {@code Rectangle} whose width or height is negative has neither
     * location nor dimension along those axes with negative dimensions.
     * Such a {@code Rectangle} is treated as non-existant along those axes.
     * Such a {@code Rectangle} is also empty with respect to containment
     * calculations and methods which test if it contains or intersects a
     * point or rectangle will always return false.
     * Methods which combine such a {@code Rectangle} with a point or rectangle
     * will ignore the {@code Rectangle} entirely in generating the result.
     * If two {@code Rectangle} objects are combined and each has a negative
     * dimension, the result will have at least one negative dimension.
     * </a>
     * <p>
     * Methods which affect only the location of a {@code Rectangle} will
     * operate on its location regardless of whether or not it has a negative
     * or zero dimension along either axis.
     * <p>
     * Note that a {@code Rectangle} constructed with the default no-argument
     * constructor will have dimensions of {@code 0x0} and therefore be empty.
     * That {@code Rectangle} will still have a location of {@code (0,0)} and
     * will contribute that location to the union and add operations.
     * Code attempting to accumulate the bounds of a set of points should
     * therefore initially construct the {@code Rectangle} with a specifically
     * negative width and height or it should use the first point in the set
     * to construct the {@code Rectangle}.
     * For example:
     * <pre>{@code
     * Rectangle bounds = new Rectangle(0, 0, -1, -1);
     * for (int i = 0; i < points.length; i++) {
     * bounds.add(points[i]);
     * }
     * }</pre>
     * or if we know that the points array contains at least one point:
     * <pre>{@code
     * Rectangle bounds = new Rectangle(points[0]);
     * for (int i = 1; i < points.length; i++) {
     * bounds.add(points[i]);
     * }
     * }</pre>
     * <p>
     * This class uses 32-bit integers to store its location and dimensions.
     * Frequently operations may produce a result that exceeds the range of
     * a 32-bit integer.
     * The methods will calculate their results in a way that avoids any
     * 32-bit overflow for intermediate results and then choose the best
     * representation to store the final results back into the 32-bit fields
     * which hold the location and dimensions.
     * The location of the result will be stored into the {@link #x} and
     * {@link #y} fields by clipping the true result to the nearest 32-bit value.
     * The values stored into the {@link #width} and {@link #height} dimension
     * fields will be chosen as the 32-bit values that encompass the largest
     * part of the true result as possible.
     * Generally this means that the dimension will be clipped independently
     * to the range of 32-bit integers except that if the location had to be
     * moved to store it into its pair of 32-bit fields then the dimensions
     * will be adjusted relative to the "best representation" of the location.
     * If the true result had a negative dimension and was therefore
     * non-existant along one or both axes, the stored dimensions will be
     * negative numbers in those axes.
     * If the true result had a location that could be represented within
     * the range of 32-bit integers, but zero dimension along one or both
     * axes, then the stored dimensions will be zero in those axes.
     *
     * @author      Sami Shaio
     * @since 1.0
     */
    class Rectangle extends java.awt.geom.Rectangle2D implements java.awt.Shape, java.io.Serializable {
        /**
         * The X coordinate of the upper-left corner of the <code>Rectangle</code>.
         *
         * @serial
         * @see #setLocation(int, int)
         * @see #getLocation()
         * @since 1.0
         */
        x: number;
        /**
         * The Y coordinate of the upper-left corner of the <code>Rectangle</code>.
         *
         * @serial
         * @see #setLocation(int, int)
         * @see #getLocation()
         * @since 1.0
         */
        y: number;
        /**
         * The width of the <code>Rectangle</code>.
         * @serial
         * @see #setSize(int, int)
         * @see #getSize()
         * @since 1.0
         */
        width: number;
        /**
         * The height of the <code>Rectangle</code>.
         *
         * @serial
         * @see #setSize(int, int)
         * @see #getSize()
         * @since 1.0
         */
        height: number;
        static serialVersionUID: number;
        /**
         * Constructs a new <code>Rectangle</code> whose upper-left corner is
         * specified as
         * {@code (x,y)} and whose width and height
         * are specified by the arguments of the same name.
         * @param     x the specified X coordinate
         * @param     y the specified Y coordinate
         * @param     width    the width of the <code>Rectangle</code>
         * @param     height   the height of the <code>Rectangle</code>
         * @since 1.0
         */
        constructor(x?: any, y?: any, width?: any, height?: any);
        /**
         * Returns the X coordinate of the bounding <code>Rectangle</code> in
         * <code>double</code> precision.
         * @return the X coordinate of the bounding <code>Rectangle</code>.
         */
        getX(): number;
        /**
         * Returns the Y coordinate of the bounding <code>Rectangle</code> in
         * <code>double</code> precision.
         * @return the Y coordinate of the bounding <code>Rectangle</code>.
         */
        getY(): number;
        /**
         * Returns the width of the bounding <code>Rectangle</code> in
         * <code>double</code> precision.
         * @return the width of the bounding <code>Rectangle</code>.
         */
        getWidth(): number;
        /**
         * Returns the height of the bounding <code>Rectangle</code> in
         * <code>double</code> precision.
         * @return the height of the bounding <code>Rectangle</code>.
         */
        getHeight(): number;
        /**
         * Gets the bounding <code>Rectangle</code> of this <code>Rectangle</code>.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>getBounds</code> method of
         * {@link Component}.
         * @return    a new <code>Rectangle</code>, equal to the
         * bounding <code>Rectangle</code> for this <code>Rectangle</code>.
         * @see       java.awt.Component#getBounds
         * @see       #setBounds(Rectangle)
         * @see       #setBounds(int, int, int, int)
         * @since     1.1
         */
        getBounds(): Rectangle;
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        getBounds2D(): java.awt.geom.Rectangle2D;
        /**
         * Sets the bounding <code>Rectangle</code> of this <code>Rectangle</code>
         * to match the specified <code>Rectangle</code>.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>setBounds</code> method of <code>Component</code>.
         * @param r the specified <code>Rectangle</code>
         * @see       #getBounds
         * @see       java.awt.Component#setBounds(java.awt.Rectangle)
         * @since     1.1
         */
        setBounds$java_awt_Rectangle(r: Rectangle): void;
        /**
         * Sets the bounding <code>Rectangle</code> of this
         * <code>Rectangle</code> to the specified
         * <code>x</code>, <code>y</code>, <code>width</code>,
         * and <code>height</code>.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>setBounds</code> method of <code>Component</code>.
         * @param x the new X coordinate for the upper-left
         * corner of this <code>Rectangle</code>
         * @param y the new Y coordinate for the upper-left
         * corner of this <code>Rectangle</code>
         * @param width the new width for this <code>Rectangle</code>
         * @param height the new height for this <code>Rectangle</code>
         * @see       #getBounds
         * @see       java.awt.Component#setBounds(int, int, int, int)
         * @since     1.1
         */
        setBounds(x?: any, y?: any, width?: any, height?: any): any;
        setRect(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * Sets the bounds of this {@code Rectangle} to the integer bounds
         * which encompass the specified {@code x}, {@code y}, {@code width},
         * and {@code height}.
         * If the parameters specify a {@code Rectangle} that exceeds the
         * maximum range of integers, the result will be the best
         * representation of the specified {@code Rectangle} intersected
         * with the maximum integer bounds.
         * @param x the X coordinate of the upper-left corner of
         * the specified rectangle
         * @param y the Y coordinate of the upper-left corner of
         * the specified rectangle
         * @param width the width of the specified rectangle
         * @param height the new height of the specified rectangle
         */
        setRect$double$double$double$double(x: number, y: number, width: number, height: number): void;
        private static clip(v, doceil);
        /**
         * Sets the bounding <code>Rectangle</code> of this
         * <code>Rectangle</code> to the specified
         * <code>x</code>, <code>y</code>, <code>width</code>,
         * and <code>height</code>.
         * <p>
         * @param x the new X coordinate for the upper-left
         * corner of this <code>Rectangle</code>
         * @param y the new Y coordinate for the upper-left
         * corner of this <code>Rectangle</code>
         * @param width the new width for this <code>Rectangle</code>
         * @param height the new height for this <code>Rectangle</code>
         * @deprecated As of JDK version 1.1,
         * replaced by <code>setBounds(int, int, int, int)</code>.
         */
        reshape(x: number, y: number, width: number, height: number): void;
        /**
         * Returns the location of this <code>Rectangle</code>.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>getLocation</code> method of <code>Component</code>.
         * @return the <code>Point</code> that is the upper-left corner of
         * this <code>Rectangle</code>.
         * @see       java.awt.Component#getLocation
         * @see       #setLocation(Point)
         * @see       #setLocation(int, int)
         * @since     1.1
         */
        getLocation(): java.awt.Point;
        /**
         * Moves this <code>Rectangle</code> to the specified location.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>setLocation</code> method of <code>Component</code>.
         * @param p the <code>Point</code> specifying the new location
         * for this <code>Rectangle</code>
         * @see       java.awt.Component#setLocation(java.awt.Point)
         * @see       #getLocation
         * @since     1.1
         */
        setLocation$java_awt_Point(p: java.awt.Point): void;
        /**
         * Moves this <code>Rectangle</code> to the specified location.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>setLocation</code> method of <code>Component</code>.
         * @param x the X coordinate of the new location
         * @param y the Y coordinate of the new location
         * @see       #getLocation
         * @see       java.awt.Component#setLocation(int, int)
         * @since     1.1
         */
        setLocation(x?: any, y?: any): any;
        /**
         * Moves this <code>Rectangle</code> to the specified location.
         * <p>
         * @param x the X coordinate of the new location
         * @param y the Y coordinate of the new location
         * @deprecated As of JDK version 1.1,
         * replaced by <code>setLocation(int, int)</code>.
         */
        move(x: number, y: number): void;
        /**
         * Translates this <code>Rectangle</code> the indicated distance,
         * to the right along the X coordinate axis, and
         * downward along the Y coordinate axis.
         * @param dx the distance to move this <code>Rectangle</code>
         * along the X axis
         * @param dy the distance to move this <code>Rectangle</code>
         * along the Y axis
         * @see       java.awt.Rectangle#setLocation(int, int)
         * @see       java.awt.Rectangle#setLocation(java.awt.Point)
         */
        translate(dx: number, dy: number): void;
        /**
         * Gets the size of this <code>Rectangle</code>, represented by
         * the returned <code>Dimension</code>.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>getSize</code> method of <code>Component</code>.
         * @return a <code>Dimension</code>, representing the size of
         * this <code>Rectangle</code>.
         * @see       java.awt.Component#getSize
         * @see       #setSize(Dimension)
         * @see       #setSize(int, int)
         * @since     1.1
         */
        getSize(): java.awt.Dimension;
        /**
         * Sets the size of this <code>Rectangle</code> to match the
         * specified <code>Dimension</code>.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>setSize</code> method of <code>Component</code>.
         * @param d the new size for the <code>Dimension</code> object
         * @see       java.awt.Component#setSize(java.awt.Dimension)
         * @see       #getSize
         * @since     1.1
         */
        setSize$java_awt_Dimension(d: java.awt.Dimension): void;
        /**
         * Sets the size of this <code>Rectangle</code> to the specified
         * width and height.
         * <p>
         * This method is included for completeness, to parallel the
         * <code>setSize</code> method of <code>Component</code>.
         * @param width the new width for this <code>Rectangle</code>
         * @param height the new height for this <code>Rectangle</code>
         * @see       java.awt.Component#setSize(int, int)
         * @see       #getSize
         * @since     1.1
         */
        setSize(width?: any, height?: any): any;
        /**
         * Sets the size of this <code>Rectangle</code> to the specified
         * width and height.
         * <p>
         * @param width the new width for this <code>Rectangle</code>
         * @param height the new height for this <code>Rectangle</code>
         * @deprecated As of JDK version 1.1,
         * replaced by <code>setSize(int, int)</code>.
         */
        resize(width: number, height: number): void;
        contains(x?: any, y?: any, w?: any, h?: any, origrect?: any): any;
        /**
         * Checks whether or not this <code>Rectangle</code> contains the
         * specified <code>Point</code>.
         * @param p the <code>Point</code> to test
         * @return    <code>true</code> if the specified <code>Point</code>
         * is inside this <code>Rectangle</code>;
         * <code>false</code> otherwise.
         * @since     1.1
         */
        contains$java_awt_Point(p: java.awt.Point): boolean;
        /**
         * Checks whether or not this <code>Rectangle</code> contains the
         * point at the specified location {@code (x,y)}.
         *
         * @param  x the specified X coordinate
         * @param  y the specified Y coordinate
         * @return    <code>true</code> if the point
         * {@code (x,y)} is inside this
         * <code>Rectangle</code>;
         * <code>false</code> otherwise.
         * @since     1.1
         */
        contains$int$int(x: number, y: number): boolean;
        /**
         * Checks whether or not this <code>Rectangle</code> entirely contains
         * the specified <code>Rectangle</code>.
         *
         * @param     r   the specified <code>Rectangle</code>
         * @return    <code>true</code> if the <code>Rectangle</code>
         * is contained entirely inside this <code>Rectangle</code>;
         * <code>false</code> otherwise
         * @since     1.2
         */
        contains$java_awt_Rectangle(r: Rectangle): boolean;
        /**
         * Checks whether this <code>Rectangle</code> entirely contains
         * the <code>Rectangle</code>
         * at the specified location {@code (X,Y)} with the
         * specified dimensions {@code (W,H)}.
         * @param     X the specified X coordinate
         * @param     Y the specified Y coordinate
         * @param     W   the width of the <code>Rectangle</code>
         * @param     H   the height of the <code>Rectangle</code>
         * @return    <code>true</code> if the <code>Rectangle</code> specified by
         * {@code (X, Y, W, H)}
         * is entirely enclosed inside this <code>Rectangle</code>;
         * <code>false</code> otherwise.
         * @since     1.1
         */
        contains$int$int$int$int(X: number, Y: number, W: number, H: number): boolean;
        /**
         * Checks whether or not this <code>Rectangle</code> contains the
         * point at the specified location {@code (X,Y)}.
         *
         * @param  X the specified X coordinate
         * @param  Y the specified Y coordinate
         * @return    <code>true</code> if the point
         * {@code (X,Y)} is inside this
         * <code>Rectangle</code>;
         * <code>false</code> otherwise.
         * @deprecated As of JDK version 1.1,
         * replaced by <code>contains(int, int)</code>.
         */
        inside(X: number, Y: number): boolean;
        /**
         * Determines whether or not this <code>Rectangle</code> and the specified
         * <code>Rectangle</code> intersect. Two rectangles intersect if
         * their intersection is nonempty.
         *
         * @param r the specified <code>Rectangle</code>
         * @return    <code>true</code> if the specified <code>Rectangle</code>
         * and this <code>Rectangle</code> intersect;
         * <code>false</code> otherwise.
         */
        intersects$java_awt_Rectangle(r: Rectangle): boolean;
        /**
         * Computes the intersection of this <code>Rectangle</code> with the
         * specified <code>Rectangle</code>. Returns a new <code>Rectangle</code>
         * that represents the intersection of the two rectangles.
         * If the two rectangles do not intersect, the result will be
         * an empty rectangle.
         *
         * @param     r   the specified <code>Rectangle</code>
         * @return    the largest <code>Rectangle</code> contained in both the
         * specified <code>Rectangle</code> and in
         * this <code>Rectangle</code>; or if the rectangles
         * do not intersect, an empty rectangle.
         */
        intersection(r: Rectangle): Rectangle;
        /**
         * Computes the union of this <code>Rectangle</code> with the
         * specified <code>Rectangle</code>. Returns a new
         * <code>Rectangle</code> that
         * represents the union of the two rectangles.
         * <p>
         * If either {@code Rectangle} has any dimension less than zero
         * the rules for <a href=#NonExistant>non-existant</a> rectangles
         * apply.
         * If only one has a dimension less than zero, then the result
         * will be a copy of the other {@code Rectangle}.
         * If both have dimension less than zero, then the result will
         * have at least one dimension less than zero.
         * <p>
         * If the resulting {@code Rectangle} would have a dimension
         * too large to be expressed as an {@code int}, the result
         * will have a dimension of {@code Integer.MAX_VALUE} along
         * that dimension.
         * @param r the specified <code>Rectangle</code>
         * @return    the smallest <code>Rectangle</code> containing both
         * the specified <code>Rectangle</code> and this
         * <code>Rectangle</code>.
         */
        union(r: Rectangle): Rectangle;
        /**
         * Adds a point, specified by the integer arguments {@code newx,newy}
         * to the bounds of this {@code Rectangle}.
         * <p>
         * If this {@code Rectangle} has any dimension less than zero,
         * the rules for <a href=#NonExistant>non-existant</a>
         * rectangles apply.
         * In that case, the new bounds of this {@code Rectangle} will
         * have a location equal to the specified coordinates and
         * width and height equal to zero.
         * <p>
         * After adding a point, a call to <code>contains</code> with the
         * added point as an argument does not necessarily return
         * <code>true</code>. The <code>contains</code> method does not
         * return <code>true</code> for points on the right or bottom
         * edges of a <code>Rectangle</code>. Therefore, if the added point
         * falls on the right or bottom edge of the enlarged
         * <code>Rectangle</code>, <code>contains</code> returns
         * <code>false</code> for that point.
         * If the specified point must be contained within the new
         * {@code Rectangle}, a 1x1 rectangle should be added instead:
         * <pre>
         * r.add(newx, newy, 1, 1);
         * </pre>
         * @param newx the X coordinate of the new point
         * @param newy the Y coordinate of the new point
         */
        add(newx?: any, newy?: any): any;
        /**
         * Adds the specified {@code Point} to the bounds of this
         * {@code Rectangle}.
         * <p>
         * If this {@code Rectangle} has any dimension less than zero,
         * the rules for <a href=#NonExistant>non-existant</a>
         * rectangles apply.
         * In that case, the new bounds of this {@code Rectangle} will
         * have a location equal to the coordinates of the specified
         * {@code Point} and width and height equal to zero.
         * <p>
         * After adding a <code>Point</code>, a call to <code>contains</code>
         * with the added <code>Point</code> as an argument does not
         * necessarily return <code>true</code>. The <code>contains</code>
         * method does not return <code>true</code> for points on the right
         * or bottom edges of a <code>Rectangle</code>. Therefore if the added
         * <code>Point</code> falls on the right or bottom edge of the
         * enlarged <code>Rectangle</code>, <code>contains</code> returns
         * <code>false</code> for that <code>Point</code>.
         * If the specified point must be contained within the new
         * {@code Rectangle}, a 1x1 rectangle should be added instead:
         * <pre>
         * r.add(pt.x, pt.y, 1, 1);
         * </pre>
         * @param pt the new <code>Point</code> to add to this
         * <code>Rectangle</code>
         */
        add$java_awt_Point(pt: java.awt.Point): void;
        /**
         * Adds a <code>Rectangle</code> to this <code>Rectangle</code>.
         * The resulting <code>Rectangle</code> is the union of the two
         * rectangles.
         * <p>
         * If either {@code Rectangle} has any dimension less than 0, the
         * result will have the dimensions of the other {@code Rectangle}.
         * If both {@code Rectangle}s have at least one dimension less
         * than 0, the result will have at least one dimension less than 0.
         * <p>
         * If either {@code Rectangle} has one or both dimensions equal
         * to 0, the result along those axes with 0 dimensions will be
         * equivalent to the results obtained by adding the corresponding
         * origin coordinate to the result rectangle along that axis,
         * similar to the operation of the {@link #add(Point)} method,
         * but contribute no further dimension beyond that.
         * <p>
         * If the resulting {@code Rectangle} would have a dimension
         * too large to be expressed as an {@code int}, the result
         * will have a dimension of {@code Integer.MAX_VALUE} along
         * that dimension.
         * @param  r the specified <code>Rectangle</code>
         */
        add$java_awt_Rectangle(r: Rectangle): void;
        /**
         * Resizes the <code>Rectangle</code> both horizontally and vertically.
         * <p>
         * This method modifies the <code>Rectangle</code> so that it is
         * <code>h</code> units larger on both the left and right side,
         * and <code>v</code> units larger at both the top and bottom.
         * <p>
         * The new <code>Rectangle</code> has {@code (x - h, y - v)}
         * as its upper-left corner,
         * width of {@code (width + 2h)},
         * and a height of {@code (height + 2v)}.
         * <p>
         * If negative values are supplied for <code>h</code> and
         * <code>v</code>, the size of the <code>Rectangle</code>
         * decreases accordingly.
         * The {@code grow} method will check for integer overflow
         * and underflow, but does not check whether the resulting
         * values of {@code width} and {@code height} grow
         * from negative to non-negative or shrink from non-negative
         * to negative.
         * @param h the horizontal expansion
         * @param v the vertical expansion
         */
        grow(h: number, v: number): void;
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        isEmpty(): boolean;
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        outcode(x?: any, y?: any): any;
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        createIntersection(r: java.awt.geom.Rectangle2D): java.awt.geom.Rectangle2D;
        /**
         * {@inheritDoc}
         * @since 1.2
         */
        createUnion(r: java.awt.geom.Rectangle2D): java.awt.geom.Rectangle2D;
        /**
         * Checks whether two rectangles are equal.
         * <p>
         * The result is <code>true</code> if and only if the argument is not
         * <code>null</code> and is a <code>Rectangle</code> object that has the
         * same upper-left corner, width, and height as
         * this <code>Rectangle</code>.
         * @param obj the <code>Object</code> to compare with
         * this <code>Rectangle</code>
         * @return    <code>true</code> if the objects are equal;
         * <code>false</code> otherwise.
         */
        equals(obj: any): boolean;
        /**
         * Returns a <code>String</code> representing this
         * <code>Rectangle</code> and its values.
         * @return a <code>String</code> representing this
         * <code>Rectangle</code> object's coordinate and size values.
         */
        toString(): string;
    }
}
declare namespace java.applet {
    class Applet {
        static __static_initialized: boolean;
        static __static_initialize(): void;
        static CURRENT_ID: number;
        static __static_initializer_0(): void;
        container: HTMLElement;
        backgroundColor: java.awt.Color;
        layout: java.awt.Layout;
        constructor();
        init(): void;
        paint(g: java.awt.Graphics): void;
        setBackground(c: java.awt.Color): void;
        setLayout(layout: java.awt.Layout): void;
        add(component: java.awt.HTMLComponent): void;
    }
}
declare namespace java.io {
    /**
     * @skip
     */
    class PrintStream extends java.io.FilterOutputStream {
        constructor(out: java.io.OutputStream);
        print$boolean(x: boolean): void;
        print$char(x: string): void;
        print(x?: any): any;
        print$double(x: number): void;
        print$float(x: number): void;
        print$int(x: number): void;
        print$long(x: number): void;
        print$java_lang_Object(x: any): void;
        print$java_lang_String(s: string): void;
        println$(): void;
        println$boolean(x: boolean): void;
        println$char(x: string): void;
        println(x?: any): any;
        println$double(x: number): void;
        println$float(x: number): void;
        println$int(x: number): void;
        println$long(x: number): void;
        println$java_lang_Object(x: any): void;
        println$java_lang_String(s: string): void;
    }
}
declare namespace java.io {
    /**
     * JSweet implementation.
     */
    class FileReader extends java.io.InputStreamReader {
        constructor(fileName?: any);
    }
}
declare namespace java.io {
    /**
     * JSweet implementation.
     */
    class FileWriter extends java.io.OutputStreamWriter {
        constructor(fileName?: any, append?: any);
    }
}
declare namespace java.io {
    /**
     * JSweet implementation.
     */
    class FileNotFoundException extends java.io.IOException {
        static serialVersionUID: number;
        constructor(s?: any);
    }
}
declare namespace java.io {
    /**
     * A character encoding is not supported - <a
     * href="http://java.sun.com/javase/6/docs/api/java/io/UnsupportedEncodingException.html">[Sun's
     * docs]</a>.
     */
    class UnsupportedEncodingException extends java.io.IOException {
        constructor(msg?: any);
    }
}
declare namespace java.awt.geom {
    /**
     * The <code>IllegalPathStateException</code> represents an
     * exception that is thrown if an operation is performed on a path
     * that is in an illegal state with respect to the particular
     * operation being performed, such as appending a path segment
     * to a {@link GeneralPath} without an initial moveto.
     */
    class IllegalPathStateException extends Error {
        /**
         * Constructs an <code>IllegalPathStateException</code> with the
         * specified detail message.
         * @param   s   the detail message
         * @since   1.2
         */
        constructor(s?: any);
    }
}
declare namespace java.io {
    /**
     * See <a
     * href="https://docs.oracle.com/javase/8/docs/api/java/io/UncheckedIOException.html">the
     * official Java API doc</a> for details.
     */
    class UncheckedIOException extends Error {
        constructor(message?: any, cause?: any);
        getCause(): java.io.IOException;
    }
}
declare namespace java.lang.annotation {
    /**
     * Indicates an attempt to access an element of an annotation that has changed
     * since it was compiled or serialized <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/AnnotationTypeMismatchException.html">[Sun
     * docs]</a>.
     */
    class AnnotationTypeMismatchException extends Error {
        constructor();
    }
}
declare namespace java.lang.annotation {
    /**
     * Indicates an attempt to access an element of an annotation that was added
     * since it was compiled or serialized <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/IncompleteAnnotationException.html">[Sun
     * docs]</a>.
     */
    class IncompleteAnnotationException extends Error {
        __annotationType: any;
        __elementName: string;
        constructor(annotationType: any, elementName: string);
        annotationType(): any;
        elementName(): string;
    }
}
declare namespace java.lang {
    /**
     * NOTE: in GWT this is only thrown for division by zero on longs and
     * BigInteger/BigDecimal.
     * <p>
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/ArithmeticException.html">the
     * official Java API doc</a> for details.
     */
    class ArithmeticException extends Error {
        constructor(explanation?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/ArrayStoreException.html">the
     * official Java API doc</a> for details.
     */
    class ArrayStoreException extends Error {
        constructor(message?: any);
    }
}
declare namespace java.lang {
    /**
     * Indicates failure to cast one type into another.
     */
    class ClassCastException extends Error {
        constructor(message?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/IllegalArgumentException.html">the
     * official Java API doc</a> for details.
     */
    class IllegalArgumentException extends Error {
        constructor(message?: any, cause?: any);
    }
}
declare namespace java.lang {
    /**
     * Indicates that an objet was in an invalid state during an attempted
     * operation.
     */
    class IllegalStateException extends Error {
        constructor(message?: any, cause?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/IndexOutOfBoundsException.html">the
     * official Java API doc</a> for details.
     */
    class IndexOutOfBoundsException extends Error {
        constructor(message?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/NegativeArraySizeException.html">the
     * official Java API doc</a> for details.
     */
    class NegativeArraySizeException extends Error {
        constructor(message?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/NullPointerException.html">the
     * official Java API doc</a> for details.
     */
    class NullPointerException extends Error {
        constructor(message?: any);
        createError(msg: string): any;
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/UnsupportedOperationException.html">the
     * official Java API doc</a> for details.
     */
    class UnsupportedOperationException extends Error {
        constructor(message?: any, cause?: any);
    }
}
declare namespace java.util {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/ConcurrentModificationException.html">the
     * official Java API doc</a> for details.
     */
    class ConcurrentModificationException extends Error {
        constructor(message?: any);
    }
}
declare namespace java.util {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/EmptyStackException.html">the
     * official Java API doc</a> for details.
     */
    class EmptyStackException extends Error {
        constructor();
    }
}
declare namespace java.util {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/MissingResourceException.html">the
     * official Java API doc</a> for details.
     */
    class MissingResourceException extends Error {
        private className;
        private key;
        constructor(s: string, className: string, key: string);
        getClassName(): string;
        getKey(): string;
    }
}
declare namespace java.util {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/NoSuchElementException.html">the
     * official Java API doc</a> for details.
     */
    class NoSuchElementException extends Error {
        constructor(s?: any);
    }
}
declare namespace java.security {
    /**
     * A generic security exception type - <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/security/DigestException.html">[Sun's
     * docs]</a>.
     */
    class DigestException extends java.security.GeneralSecurityException {
        constructor(msg?: any);
    }
}
declare namespace java.security {
    /**
     * A generic security exception type - <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/security/NoSuchAlgorithmException.html">[Sun's
     * docs]</a>.
     */
    class NoSuchAlgorithmException extends java.security.GeneralSecurityException {
        constructor(msg?: any);
    }
}
declare namespace java.nio.charset {
    /**
     * Constant definitions for the standard Charsets.
     */
    class StandardCharsets {
        static ISO_8859_1: java.nio.charset.Charset;
        static ISO_8859_1_$LI$(): java.nio.charset.Charset;
        static UTF_8: java.nio.charset.Charset;
        static UTF_8_$LI$(): java.nio.charset.Charset;
        constructor();
    }
}
declare namespace java.util {
    /**
     * Skeletal implementation of the List interface. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/AbstractSequentialList.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    abstract class AbstractSequentialList<E> extends java.util.AbstractList<E> {
        constructor();
        add(index?: any, element?: any): any;
        addAll(index?: any, c?: any): any;
        get(index: number): E;
        iterator(): java.util.Iterator<E>;
        listIterator(index?: any): any;
        remove(index?: any): any;
        set(index: number, element: E): E;
        abstract size(): number;
    }
}
declare namespace java.util {
    /**
     * Resizeable array implementation of the List interface. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/ArrayList.html">[Sun
     * docs]</a>
     *
     * <p>
     * This implementation differs from JDK 1.5 <code>ArrayList</code> in terms of
     * capacity management. There is no speed advantage to pre-allocating array
     * sizes in JavaScript, so this implementation does not include any of the
     * capacity and "growth increment" concepts in the standard ArrayList class.
     * Although <code>ArrayList(int)</code> accepts a value for the initial
     * capacity of the array, this constructor simply delegates to
     * <code>ArrayList()</code>. It is only present for compatibility with JDK
     * 1.5's API.
     * </p>
     *
     * @param <E> the element type.
     */
    class ArrayList<E> extends java.util.AbstractList<E> implements java.util.List<E>, java.lang.Cloneable, java.util.RandomAccess, java.io.Serializable {
        forEach(action: (p1: any) => void): void;
        /**
         * This field holds a JavaScript array.
         */
        private array;
        /**
         * Ensures that RPC will consider type parameter E to be exposed. It will be
         * pruned by dead code elimination.
         */
        private exposeElement;
        constructor(c?: any);
        add$java_lang_Object(o: E): boolean;
        add(index?: any, o?: any): any;
        addAll$java_util_Collection(c: java.util.Collection<any>): boolean;
        addAll(index?: any, c?: any): any;
        clear(): void;
        clone(): any;
        contains(o: any): boolean;
        ensureCapacity(ignored: number): void;
        get(index: number): E;
        indexOf$java_lang_Object(o: any): number;
        iterator(): java.util.Iterator<E>;
        isEmpty(): boolean;
        lastIndexOf$java_lang_Object(o: any): number;
        remove(index?: any): any;
        remove$java_lang_Object(o: any): boolean;
        set(index: number, o: E): E;
        size(): number;
        toArray$(): any[];
        toArray<T>(out?: any): any;
        trimToSize(): void;
        removeRange(fromIndex: number, endIndex: number): void;
        /**
         * Used by Vector.
         */
        indexOf(o?: any, index?: any): any;
        /**
         * Used by Vector.
         */
        lastIndexOf(o?: any, index?: any): any;
        setSize(newSize: number): void;
    }
    namespace ArrayList {
        class ArrayList$0 implements java.util.Iterator<any> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            i: number;
            last: number;
            hasNext(): boolean;
            next(): any;
            remove(): void;
            constructor(__parent: any);
        }
    }
}
declare namespace java.util {
    /**
     * Utility methods related to native arrays. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Arrays.html">[Sun
     * docs]</a>
     */
    class Arrays {
        static asList<T>(...array: T[]): java.util.List<T>;
        /**
         * Perform a binary search on a sorted byte array.
         *
         * @param sortedArray byte array to search
         * @param key value to search for
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         */
        static binarySearch$byte_A$byte(sortedArray: number[], key: number): number;
        /**
         * Perform a binary search on a sorted char array.
         *
         * @param a char array to search
         * @param key value to search for
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         */
        static binarySearch$char_A$char(a: string[], key: string): number;
        /**
         * Perform a binary search on a sorted double array.
         *
         * @param sortedArray double array to search
         * @param key value to search for
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         */
        static binarySearch$double_A$double(sortedArray: number[], key: number): number;
        /**
         * Perform a binary search on a sorted float array.
         *
         * Note that some underlying JavaScript interpreters do not actually implement
         * floats (using double instead), so you may get slightly different behavior
         * regarding values that are very close (or equal) since conversion errors
         * to/from double may change the values slightly.
         *
         * @param sortedArray float array to search
         * @param key value to search for
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         */
        static binarySearch$float_A$float(sortedArray: number[], key: number): number;
        /**
         * Perform a binary search on a sorted int array.
         *
         * @param sortedArray int array to search
         * @param key value to search for
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         */
        static binarySearch$int_A$int(sortedArray: number[], key: number): number;
        /**
         * Perform a binary search on a sorted long array.
         *
         * Note that most underlying JavaScript interpreters do not actually implement
         * longs, so the values must be stored in doubles instead. This means that
         * certain legal values cannot be represented, and comparison of two unequal
         * long values may result in unexpected results if they are not also
         * representable as doubles.
         *
         * @param sortedArray long array to search
         * @param key value to search for
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         */
        static binarySearch$long_A$long(sortedArray: number[], key: number): number;
        /**
         * Perform a binary search on a sorted object array, using natural ordering.
         *
         * @param sortedArray object array to search
         * @param key value to search for
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         * @throws ClassCastException if <code>key</code> is not comparable to
         * <code>sortedArray</code>'s elements.
         */
        static binarySearch$java_lang_Object_A$java_lang_Object(sortedArray: any[], key: any): number;
        /**
         * Perform a binary search on a sorted short array.
         *
         * @param sortedArray short array to search
         * @param key value to search for
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         */
        static binarySearch$short_A$short(sortedArray: number[], key: number): number;
        /**
         * Perform a binary search on a sorted object array, using a user-specified
         * comparison function.
         *
         * @param sortedArray object array to search
         * @param key value to search for
         * @param comparator comparision function, <code>null</code> indicates
         * <i>natural ordering</i> should be used.
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         * @throws ClassCastException if <code>key</code> and
         * <code>sortedArray</code>'s elements cannot be compared by
         * <code>comparator</code>.
         */
        static binarySearch<T>(sortedArray?: any, key?: any, comparator?: any): any;
        static copyOf(original?: any, newLength?: any): any;
        static copyOf$byte_A$int(original: number[], newLength: number): number[];
        static copyOf$char_A$int(original: string[], newLength: number): string[];
        static copyOf$double_A$int(original: number[], newLength: number): number[];
        static copyOf$float_A$int(original: number[], newLength: number): number[];
        static copyOf$int_A$int(original: number[], newLength: number): number[];
        static copyOf$long_A$int(original: number[], newLength: number): number[];
        static copyOf$short_A$int(original: number[], newLength: number): number[];
        static copyOf$java_lang_Object_A$int<T>(original: T[], newLength: number): T[];
        static copyOfRange(original?: any, from?: any, to?: any): any;
        static copyOfRange$byte_A$int$int(original: number[], from: number, to: number): number[];
        static copyOfRange$char_A$int$int(original: string[], from: number, to: number): string[];
        static copyOfRange$double_A$int$int(original: number[], from: number, to: number): number[];
        static copyOfRange$float_A$int$int(original: number[], from: number, to: number): number[];
        static copyOfRange$int_A$int$int(original: number[], from: number, to: number): number[];
        static copyOfRange$long_A$int$int(original: number[], from: number, to: number): number[];
        static copyOfRange$short_A$int$int(original: number[], from: number, to: number): number[];
        static copyOfRange$java_lang_Object_A$int$int<T>(original: T[], from: number, to: number): T[];
        static deepEquals(a1: any[], a2: any[]): boolean;
        static deepHashCode(a: any[]): number;
        static deepToString$java_lang_Object_A(a: any[]): string;
        static equals(array1?: any, array2?: any): any;
        static equals$byte_A$byte_A(array1: number[], array2: number[]): boolean;
        static equals$char_A$char_A(array1: string[], array2: string[]): boolean;
        static equals$double_A$double_A(array1: number[], array2: number[]): boolean;
        static equals$float_A$float_A(array1: number[], array2: number[]): boolean;
        static equals$int_A$int_A(array1: number[], array2: number[]): boolean;
        static equals$long_A$long_A(array1: number[], array2: number[]): boolean;
        static equals$java_lang_Object_A$java_lang_Object_A(array1: any[], array2: any[]): boolean;
        static equals$short_A$short_A(array1: number[], array2: number[]): boolean;
        static fill$boolean_A$boolean(a: boolean[], val: boolean): void;
        static fill(a?: any, fromIndex?: any, toIndex?: any, val?: any): any;
        static fill$byte_A$byte(a: number[], val: number): void;
        static fill$byte_A$int$int$byte(a: number[], fromIndex: number, toIndex: number, val: number): void;
        static fill$char_A$char(a: string[], val: string): void;
        static fill$char_A$int$int$char(a: string[], fromIndex: number, toIndex: number, val: string): void;
        static fill$double_A$double(a: number[], val: number): void;
        static fill$double_A$int$int$double(a: number[], fromIndex: number, toIndex: number, val: number): void;
        static fill$float_A$float(a: number[], val: number): void;
        static fill$float_A$int$int$float(a: number[], fromIndex: number, toIndex: number, val: number): void;
        static fill$int_A$int(a: number[], val: number): void;
        static fill$int_A$int$int$int(a: number[], fromIndex: number, toIndex: number, val: number): void;
        static fill$long_A$int$int$long(a: number[], fromIndex: number, toIndex: number, val: number): void;
        static fill$long_A$long(a: number[], val: number): void;
        static fill$java_lang_Object_A$int$int$java_lang_Object(a: any[], fromIndex: number, toIndex: number, val: any): void;
        static fill$java_lang_Object_A$java_lang_Object(a: any[], val: any): void;
        static fill$short_A$int$int$short(a: number[], fromIndex: number, toIndex: number, val: number): void;
        static fill$short_A$short(a: number[], val: number): void;
        static hashCode(a?: any): any;
        static hashCode$byte_A(a: number[]): number;
        static hashCode$char_A(a: string[]): number;
        static hashCode$double_A(a: number[]): number;
        static hashCode$float_A(a: number[]): number;
        static hashCode$int_A(a: number[]): number;
        static hashCode$long_A(a: number[]): number;
        static hashCode$java_lang_Object_A(a: any[]): number;
        static hashCode$short_A(a: number[]): number;
        static sort$byte_A(array: number[]): void;
        static sort$byte_A$int$int(array: number[], fromIndex: number, toIndex: number): void;
        static sort$char_A(array: string[]): void;
        static sort$char_A$int$int(array: string[], fromIndex: number, toIndex: number): void;
        static sort$double_A(array: number[]): void;
        static sort$double_A$int$int(array: number[], fromIndex: number, toIndex: number): void;
        static sort$float_A(array: number[]): void;
        static sort$float_A$int$int(array: number[], fromIndex: number, toIndex: number): void;
        static sort$int_A(array: number[]): void;
        static sort$int_A$int$int(array: number[], fromIndex: number, toIndex: number): void;
        static sort$long_A(array: number[]): void;
        static sort$long_A$int$int(array: number[], fromIndex: number, toIndex: number): void;
        static sort$java_lang_Object_A(array: any[]): void;
        static sort$java_lang_Object_A$int$int(x: any[], fromIndex: number, toIndex: number): void;
        static sort$short_A(array: number[]): void;
        static sort$short_A$int$int(array: number[], fromIndex: number, toIndex: number): void;
        static sort$java_lang_Object_A$java_util_Comparator<T>(x: T[], c: java.util.Comparator<any>): void;
        static sort<T>(x?: any, fromIndex?: any, toIndex?: any, c?: any): any;
        static toString(a?: any): any;
        static toString$byte_A(a: number[]): string;
        static toString$char_A(a: string[]): string;
        static toString$double_A(a: number[]): string;
        static toString$float_A(a: number[]): string;
        static toString$int_A(a: number[]): string;
        static toString$long_A(a: number[]): string;
        static toString$java_lang_Object_A(x: any[]): string;
        static toString$short_A(a: number[]): string;
        /**
         * Recursive helper function for {@link Arrays#deepToString(Object[])}.
         */
        static deepToString(a?: any, arraysIveSeen?: any): any;
        static getCopyLength(array: any, from: number, to: number): number;
        /**
         * Sort a small subsection of an array by insertion sort.
         *
         * @param array array to sort
         * @param low lower bound of range to sort
         * @param high upper bound of range to sort
         * @param comp comparator to use
         */
        static insertionSort(array: any[], low: number, high: number, comp: java.util.Comparator<any>): void;
        /**
         * Merge the two sorted subarrays (srcLow,srcMid] and (srcMid,srcHigh] into
         * dest.
         *
         * @param src source array for merge
         * @param srcLow lower bound of bottom sorted half
         * @param srcMid upper bound of bottom sorted half & lower bound of top sorted
         * half
         * @param srcHigh upper bound of top sorted half
         * @param dest destination array for merge
         * @param destLow lower bound of destination
         * @param destHigh upper bound of destination
         * @param comp comparator to use
         */
        static merge(src: any[], srcLow: number, srcMid: number, srcHigh: number, dest: any[], destLow: number, destHigh: number, comp: java.util.Comparator<any>): void;
        /**
         * Performs a merge sort on the specified portion of an object array.
         *
         * Uses O(n) temporary space to perform the merge, but is stable.
         */
        static mergeSort$java_lang_Object_A$int$int$java_util_Comparator(x: any[], fromIndex: number, toIndex: number, comp: java.util.Comparator<any>): void;
        /**
         * Recursive helper function for
         * {@link Arrays#mergeSort(Object[], int, int, Comparator)}.
         *
         * @param temp temporary space, as large as the range of elements being
         * sorted. On entry, temp should contain a copy of the sort range
         * from array.
         * @param array array to sort
         * @param low lower bound of range to sort
         * @param high upper bound of range to sort
         * @param ofs offset to convert an array index into a temp index
         * @param comp comparison function
         */
        static mergeSort(temp?: any, array?: any, low?: any, high?: any, ofs?: any, comp?: any): any;
        /**
         * Sort an entire array of number primitives.
         */
        static nativeLongSort$java_lang_Object$java_lang_Object(array: any, compareFunction: any): void;
        /**
         * Sort a subset of an array of number primitives.
         */
        static nativeLongSort(array?: any, fromIndex?: any, toIndex?: any): any;
        /**
         * Sort an entire array of number primitives.
         */
        static nativeNumberSort$java_lang_Object(array: any): void;
        /**
         * Sort a subset of an array of number primitives.
         */
        static nativeNumberSort(array?: any, fromIndex?: any, toIndex?: any): any;
    }
    namespace Arrays {
        class ArrayList<E> extends java.util.AbstractList<E> implements java.util.RandomAccess, java.io.Serializable {
            /**
             * The only reason this is non-final is so that E[] (and E) will be exposed
             * for serialization.
             */
            array: E[];
            constructor(array: E[]);
            contains(o: any): boolean;
            get(index: number): E;
            set(index: number, value: E): E;
            size(): number;
            toArray$(): any[];
            toArray<T>(out?: any): any;
        }
    }
}
declare namespace java.util {
    /**
     * To keep performance characteristics in line with Java community expectations,
     * <code>Vector</code> is a wrapper around <code>ArrayList</code>. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Vector.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    class Vector<E> extends java.util.AbstractList<E> implements java.util.List<E>, java.util.RandomAccess, java.lang.Cloneable, java.io.Serializable {
        forEach(action: (p1: any) => void): void;
        private arrayList;
        /**
         * Ensures that RPC will consider type parameter E to be exposed. It will be
         * pruned by dead code elimination.
         */
        private exposeElement;
        /**
         * Capacity increment is ignored.
         */
        constructor(initialCapacity?: any, ignoredCapacityIncrement?: any);
        add$java_lang_Object(o: E): boolean;
        add(index?: any, o?: any): any;
        addAll$java_util_Collection(c: java.util.Collection<any>): boolean;
        addAll(index?: any, c?: any): any;
        addElement(o: E): void;
        capacity(): number;
        clear(): void;
        clone(): any;
        contains(elem: any): boolean;
        containsAll(c: java.util.Collection<any>): boolean;
        copyInto(objs: any[]): void;
        elementAt(index: number): E;
        elements(): java.util.Enumeration<E>;
        ensureCapacity(capacity: number): void;
        firstElement(): E;
        get(index: number): E;
        indexOf$java_lang_Object(elem: any): number;
        indexOf(elem?: any, index?: any): any;
        insertElementAt(o: E, index: number): void;
        isEmpty(): boolean;
        iterator(): java.util.Iterator<E>;
        lastElement(): E;
        lastIndexOf$java_lang_Object(o: any): number;
        lastIndexOf(o?: any, index?: any): any;
        remove(index?: any): any;
        removeAll(c: java.util.Collection<any>): boolean;
        removeAllElements(): void;
        removeElement(o: any): boolean;
        removeElementAt(index: number): void;
        set(index: number, elem: E): E;
        setElementAt(o: E, index: number): void;
        setSize(size: number): void;
        size(): number;
        subList(fromIndex: number, toIndex: number): java.util.List<E>;
        toArray$(): any[];
        toArray<T>(a?: any): any;
        toString(): string;
        trimToSize(): void;
        removeRange(fromIndex: number, endIndex: number): void;
        private static checkArrayElementIndex(index, size);
        private static checkArrayIndexOutOfBounds(expression, index);
    }
}
declare namespace java.util {
    /**
     * An unbounded priority queue based on a priority heap. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/PriorityQueue.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    class PriorityQueue<E> extends java.util.AbstractQueue<E> {
        private static getLeftChild(node);
        private static getParent(node);
        private static getRightChild(node);
        private static isLeaf(node, size);
        private cmp;
        /**
         * A heap held in an array. heap[0] is the root of the heap (the smallest
         * element), the subtrees of node i are 2*i+1 (left) and 2*i+2 (right). Node i
         * is a leaf node if 2*i>=n. Node i's parent, if i>0, is floor((i-1)/2).
         */
        private heap;
        constructor(initialCapacity?: any, cmp?: any);
        addAll(index?: any, c?: any): any;
        addAll$java_util_Collection(c: java.util.Collection<any>): boolean;
        clear(): void;
        comparator(): java.util.Comparator<any>;
        contains(o: any): boolean;
        containsAll(c: java.util.Collection<any>): boolean;
        isEmpty(): boolean;
        iterator(): java.util.Iterator<E>;
        offer(e: E): boolean;
        peek(): E;
        poll(): E;
        remove(index?: any): any;
        remove$java_lang_Object(o: any): boolean;
        removeAll(c: java.util.Collection<any>): boolean;
        retainAll(c: java.util.Collection<any>): boolean;
        size(): number;
        toArray$(): any[];
        toArray<T>(a?: any): any;
        toString(): string;
        /**
         * Make the subtree rooted at <code>node</code> a valid heap. O(n) time
         *
         * @param node
         */
        makeHeap(node: number): void;
        /**
         * Merge two subheaps into a single heap. O(log n) time
         *
         * PRECONDITION: both children of <code>node</code> are heaps
         *
         * @param node the parent of the two subtrees to merge
         */
        mergeHeaps(node: number): void;
        private getSmallestChild(node, heapSize);
        private isLeaf(node);
        private removeAtIndex(index);
    }
}
declare namespace java.util {
    /**
     * Skeletal implementation of the Map interface.
     * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/AbstractMap.html">
     * [Sun docs]</a>
     *
     * @param <K>
     * the key type.
     * @param <V>
     * the value type.
     */
    abstract class AbstractMap<K, V> implements java.util.Map<K, V> {
        constructor();
        clear(): void;
        containsKey(key: any): boolean;
        containsValue(value: any): boolean;
        containsEntry(entry: Map.Entry<any, any>): boolean;
        abstract entrySet(): java.util.Set<Map.Entry<K, V>>;
        equals(obj: any): boolean;
        get(key: any): V;
        hashCode(): number;
        isEmpty(): boolean;
        keySet(): java.util.Set<K>;
        put(key?: any, value?: any): any;
        put$java_lang_Object$java_lang_Object(key: K, value: V): V;
        putAll(map: java.util.Map<any, any>): void;
        remove(key: any): V;
        size(): number;
        toString$(): string;
        toString(entry?: any): any;
        toString$java_lang_Object(o: any): string;
        values(): java.util.Collection<V>;
        static getEntryKeyOrNull<K, V>(entry: Map.Entry<K, V>): K;
        static getEntryValueOrNull<K, V>(entry: Map.Entry<K, V>): V;
        implFindEntry(key: any, remove: boolean): Map.Entry<K, V>;
    }
    namespace AbstractMap {
        /**
         * Basic {@link Map.Entry} implementation used by {@link SimpleEntry} and
         * {@link SimpleImmutableEntry}.
         */
        abstract class AbstractEntry<K, V> implements Map.Entry<K, V> {
            key: K;
            value: V;
            constructor(key: K, value: V);
            getKey(): K;
            getValue(): V;
            setValue(value: V): V;
            equals(other: any): boolean;
            /**
             * Calculate the hash code using Sun's specified algorithm.
             */
            hashCode(): number;
            toString(): string;
        }
        /**
         * A mutable {@link Map.Entry} shared by several {@link Map}
         * implementations.
         */
        class SimpleEntry<K, V> extends AbstractMap.AbstractEntry<K, V> {
            constructor(key?: any, value?: any);
        }
        /**
         * An immutable {@link Map.Entry} shared by several {@link Map}
         * implementations.
         */
        class SimpleImmutableEntry<K, V> extends AbstractMap.AbstractEntry<K, V> {
            constructor(key?: any, value?: any);
            setValue(value: V): V;
        }
        class AbstractMap$0 extends java.util.AbstractSet<any> {
            __parent: any;
            clear(): void;
            contains(key: any): boolean;
            iterator(): java.util.Iterator<any>;
            remove(key: any): boolean;
            size(): number;
            constructor(__parent: any);
        }
        namespace AbstractMap$0 {
            class AbstractMap$0$0 implements java.util.Iterator<any> {
                private outerIter;
                __parent: any;
                forEachRemaining(consumer: (p1: any) => void): void;
                hasNext(): boolean;
                next(): any;
                remove(): void;
                constructor(__parent: any, outerIter: any);
            }
        }
        class AbstractMap$1 extends java.util.AbstractCollection<any> {
            __parent: any;
            clear(): void;
            contains(value: any): boolean;
            iterator(): java.util.Iterator<any>;
            size(): number;
            constructor(__parent: any);
        }
        namespace AbstractMap$1 {
            class AbstractMap$1$0 implements java.util.Iterator<any> {
                private outerIter;
                __parent: any;
                forEachRemaining(consumer: (p1: any) => void): void;
                hasNext(): boolean;
                next(): any;
                remove(): void;
                constructor(__parent: any, outerIter: any);
            }
        }
    }
}
declare namespace java.util {
    /**
     * A {@link java.util.Set} of {@link Enum}s. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/EnumSet.html">[Sun
     * docs]</a>
     *
     * @param <E> enumeration type
     */
    abstract class EnumSet<E extends java.lang.Enum<E>> extends java.util.AbstractSet<E> {
        static allOf<E extends java.lang.Enum<E>>(elementType: any): EnumSet<E>;
        static complementOf<E extends java.lang.Enum<E>>(other: EnumSet<E>): EnumSet<E>;
        static copyOf$java_util_Collection<E extends java.lang.Enum<E>>(c: java.util.Collection<E>): EnumSet<E>;
        static copyOf<E extends java.lang.Enum<E>>(s?: any): any;
        static noneOf<E extends java.lang.Enum<E>>(elementType: any): EnumSet<E>;
        static of$java_lang_Enum<E extends java.lang.Enum<E>>(first: E): EnumSet<E>;
        static of<E extends java.lang.Enum<E>>(first?: any, ...rest: any[]): any;
        static range<E extends java.lang.Enum<E>>(from: E, to: E): EnumSet<E>;
        /**
         * Single implementation only.
         */
        constructor();
        abstract clone(): EnumSet<E>;
        abstract capacity(): number;
    }
    namespace EnumSet {
        /**
         * Implemented via sparse array since the set size is finite. Iteration takes
         * linear time with respect to the set of the enum rather than the number of
         * items in the set.
         *
         * Note: Implemented as a subclass instead of a concrete final EnumSet class.
         * This is because declaring an EnumSet.add(E) causes hosted mode to bind to
         * the tighter method rather than the bridge method; but the tighter method
         * isn't available in the real JRE.
         */
        class EnumSetImpl<E extends java.lang.Enum<E>> extends java.util.EnumSet<E> {
            /**
             * All enums; reference to the class's copy; must not be modified.
             */
            all: E[];
            /**
             * Live enums in the set.
             */
            set: E[];
            /**
             * Count of enums in the set.
             */
            __size: number;
            /**
             * Constructs a set taking ownership of the specified set. The size must
             * accurately reflect the number of non-null items in set.
             */
            constructor(all: E[], set: E[], size: number);
            add(index?: any, element?: any): any;
            add$java_lang_Enum(e: E): boolean;
            clone(): java.util.EnumSet<E>;
            contains(o: any): boolean;
            containsEnum(e: java.lang.Enum<any>): boolean;
            iterator(): java.util.Iterator<E>;
            remove(index?: any): any;
            remove$java_lang_Object(o: any): boolean;
            removeEnum(e: java.lang.Enum<any>): boolean;
            size(): number;
            capacity(): number;
        }
        namespace EnumSetImpl {
            class IteratorImpl implements java.util.Iterator<any> {
                __parent: any;
                forEachRemaining(consumer: (p1: any) => void): void;
                i: number;
                last: number;
                constructor(__parent: any);
                hasNext(): boolean;
                next(): any;
                remove(): void;
                findNext(): void;
            }
        }
    }
}
declare namespace java.util {
    /**
     * Implements a set in terms of a hash table. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/HashSet.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    class HashSet<E> extends java.util.AbstractSet<E> implements java.util.Set<E>, java.lang.Cloneable, java.io.Serializable {
        forEach(action: (p1: any) => void): void;
        private map;
        /**
         * Ensures that RPC will consider type parameter E to be exposed. It will be
         * pruned by dead code elimination.
         */
        private exposeElement;
        constructor(initialCapacity?: any, loadFactor?: any);
        add(index?: any, element?: any): any;
        add$java_lang_Object(o: E): boolean;
        clear(): void;
        clone(): any;
        contains(o: any): boolean;
        isEmpty(): boolean;
        iterator(): java.util.Iterator<E>;
        remove(index?: any): any;
        remove$java_lang_Object(o: any): boolean;
        size(): number;
        toString(): string;
    }
}
declare namespace java.util {
    /**
     * Implements a set using a TreeMap. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/TreeSet.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    class TreeSet<E> extends java.util.AbstractSet<E> implements java.util.NavigableSet<E>, java.io.Serializable {
        forEach(action: (p1: any) => void): void;
        /**
         * TreeSet is stored as a TreeMap of the requested type to a constant Boolean.
         */
        private map;
        constructor(c?: any);
        add(index?: any, element?: any): any;
        add$java_lang_Object(o: E): boolean;
        ceiling(e: E): E;
        clear(): void;
        comparator(): java.util.Comparator<any>;
        contains(o: any): boolean;
        descendingIterator(): java.util.Iterator<E>;
        descendingSet(): java.util.NavigableSet<E>;
        first(): E;
        floor(e: E): E;
        headSet$java_lang_Object(toElement: E): java.util.SortedSet<E>;
        headSet(toElement?: any, inclusive?: any): any;
        higher(e: E): E;
        iterator(): java.util.Iterator<E>;
        last(): E;
        lower(e: E): E;
        pollFirst(): E;
        pollLast(): E;
        remove(index?: any): any;
        remove$java_lang_Object(o: any): boolean;
        size(): number;
        subSet(fromElement?: any, fromInclusive?: any, toElement?: any, toInclusive?: any): any;
        subSet$java_lang_Object$java_lang_Object(fromElement: E, toElement: E): java.util.SortedSet<E>;
        tailSet$java_lang_Object(fromElement: E): java.util.SortedSet<E>;
        tailSet(fromElement?: any, inclusive?: any): any;
    }
}
declare namespace java.lang {
    /**
     * General-purpose low-level utility methods. GWT only supports a limited subset
     * of these methods due to browser limitations. Only the documented methods are
     * available.
     */
    class System {
        /**
         * Does nothing in web mode. To get output in web mode, subclass PrintStream
         * and call {@link #setErr(PrintStream)}.
         */
        static err: java.io.PrintStream;
        static err_$LI$(): java.io.PrintStream;
        /**
         * Does nothing in web mode. To get output in web mode, subclass
         * {@link PrintStream} and call {@link #setOut(PrintStream)}.
         */
        static out: java.io.PrintStream;
        static out_$LI$(): java.io.PrintStream;
        static arraycopy(src: any, srcOfs: number, dest: any, destOfs: number, len: number): void;
        static currentTimeMillis(): number;
        /**
         * Has no effect; just here for source compatibility.
         *
         * @skip
         */
        static gc(): void;
        /**
         * The compiler replaces getProperty by the actual value of the property.
         */
        static getProperty$java_lang_String(key: string): string;
        /**
         * The compiler replaces getProperty by the actual value of the property.
         */
        static getProperty(key?: any, def?: any): any;
        static identityHashCode(o: any): number;
        static setErr(err: java.io.PrintStream): void;
        static setOut(out: java.io.PrintStream): void;
        private static arrayTypeMatch(srcComp, destComp);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/NumberFormatException.html">the
     * official Java API doc</a> for details.
     */
    class NumberFormatException extends java.lang.IllegalArgumentException {
        static forInputString(s: string): java.lang.NumberFormatException;
        static forNullInputString(): java.lang.NumberFormatException;
        static forRadix(radix: number): java.lang.NumberFormatException;
        constructor(message?: any);
    }
}
declare namespace java.nio.charset {
    /**
     * GWT emulation of {@link IllegalCharsetNameException}.
     */
    class IllegalCharsetNameException extends java.lang.IllegalArgumentException {
        private charsetName;
        constructor(charsetName: string);
        getCharsetName(): string;
    }
}
declare namespace java.nio.charset {
    /**
     * GWT emulation of {@link UnsupportedCharsetException}.
     */
    class UnsupportedCharsetException extends java.lang.IllegalArgumentException {
        private charsetName;
        constructor(charsetName: string);
        getCharsetName(): string;
    }
}
declare namespace java.lang {
    /**
     * NOTE: in GWT this will never be thrown for normal array accesses, only for
     * explicit throws.
     *
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/ArrayIndexOutOfBoundsException.html">the
     * official Java API doc</a> for details.
     */
    class ArrayIndexOutOfBoundsException extends java.lang.IndexOutOfBoundsException {
        constructor(msg?: any);
    }
}
declare namespace java.lang {
    /**
     * See <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/StringIndexOfBoundsException.html">the
     * official Java API doc</a> for details.
     */
    class StringIndexOutOfBoundsException extends java.lang.IndexOutOfBoundsException {
        constructor(message?: any);
    }
}
declare namespace java.util {
    /**
     * Linked list implementation.
     * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/LinkedList.html">
     * [Sun docs]</a>
     *
     * @param <E>
     * element type.
     */
    class LinkedList<E> extends java.util.AbstractSequentialList<E> implements java.lang.Cloneable, java.util.List<E>, java.util.Deque<E>, java.io.Serializable {
        forEach(action: (p1: any) => void): void;
        /**
         * Ensures that RPC will consider type parameter E to be exposed. It will be
         * pruned by dead code elimination.
         */
        private exposeElement;
        /**
         * Header node - header.next is the first element of the list.
         */
        private header;
        /**
         * Tail node - tail.prev is the last element of the list.
         */
        private tail;
        /**
         * Number of nodes currently present in the list.
         */
        private __size;
        constructor(c?: any);
        add$java_lang_Object(o: E): boolean;
        addFirst(o: E): void;
        addLast(o: E): void;
        clear(): void;
        reset(): void;
        clone(): any;
        descendingIterator(): java.util.Iterator<E>;
        element(): E;
        getFirst(): E;
        getLast(): E;
        listIterator(index?: any): any;
        offer(o: E): boolean;
        offerFirst(e: E): boolean;
        offerLast(e: E): boolean;
        peek(): E;
        peekFirst(): E;
        peekLast(): E;
        poll(): E;
        pollFirst(): E;
        pollLast(): E;
        pop(): E;
        push(e: E): void;
        remove$(): E;
        removeFirst(): E;
        removeFirstOccurrence(o: any): boolean;
        removeLast(): E;
        removeLastOccurrence(o: any): boolean;
        size(): number;
        addNode(o: E, prev: LinkedList.Node<E>, next: LinkedList.Node<E>): void;
        removeNode(node: LinkedList.Node<E>): E;
    }
    namespace LinkedList {
        class DescendingIteratorImpl implements java.util.Iterator<any> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            itr: java.util.ListIterator<any>;
            hasNext(): boolean;
            next(): any;
            remove(): void;
            constructor(__parent: any);
        }
        /**
         * Implementation of ListIterator for linked lists.
         */
        class ListIteratorImpl2 implements java.util.ListIterator<any> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            /**
             * The index to the current position.
             */
            currentIndex: number;
            /**
             * Current node, to be returned from next.
             */
            currentNode: LinkedList.Node<any>;
            /**
             * The last node returned from next/previous, or null if deleted or
             * never called.
             */
            lastNode: LinkedList.Node<any>;
            /**
             * @param index
             * from the beginning of the list (0 = first node)
             * @param startNode
             * the initial current node
             */
            constructor(__parent: any, index: number, startNode: LinkedList.Node<any>);
            add(o: any): void;
            hasNext(): boolean;
            hasPrevious(): boolean;
            next(): any;
            nextIndex(): number;
            previous(): any;
            previousIndex(): number;
            remove(): void;
            set(o: any): void;
        }
        /**
         * Internal class representing a doubly-linked list node.
         *
         * @param <E>
         * element type
         */
        class Node<E> {
            next: LinkedList.Node<E>;
            prev: LinkedList.Node<E>;
            value: E;
            constructor();
        }
    }
}
declare namespace java.awt.geom {
    /**
     * An <code>Area</code> object stores and manipulates a resolution-independent
     * description of an enclosed area of 2-dimensional space. <code>Area</code>
     * objects can be transformed and can perform various Constructive Area Geometry
     * (CAG) operations when combined with other <code>Area</code> objects. The CAG
     * operations include area {@link #add addition}, {@link #subtract subtraction},
     * {@link #intersect intersection}, and {@link #exclusiveOr exclusive or}. See
     * the linked method documentation for examples of the various operations.
     * <p>
     * The <code>Area</code> class implements the <code>Shape</code> interface and
     * provides full support for all of its hit-testing and path iteration
     * facilities, but an <code>Area</code> is more specific than a generalized path
     * in a number of ways:
     * <ul>
     * <li>Only closed paths and sub-paths are stored. <code>Area</code> objects
     * constructed from unclosed paths are implicitly closed during construction as
     * if those paths had been filled by the <code>Graphics2D.fill</code> method.
     * <li>The interiors of the individual stored sub-paths are all non-empty and
     * non-overlapping. Paths are decomposed during construction into separate
     * component non-overlapping parts, empty pieces of the path are discarded, and
     * then these non-empty and non-overlapping properties are maintained through
     * all subsequent CAG operations. Outlines of different component sub-paths may
     * touch each other, as long as they do not cross so that their enclosed areas
     * overlap.
     * <li>The geometry of the path describing the outline of the <code>Area</code>
     * resembles the path from which it was constructed only in that it describes
     * the same enclosed 2-dimensional area, but may use entirely different types
     * and ordering of the path segments to do so.
     * </ul>
     * Interesting issues which are not always obvious when using the
     * <code>Area</code> include:
     * <ul>
     * <li>Creating an <code>Area</code> from an unclosed (open) <code>Shape</code>
     * results in a closed outline in the <code>Area</code> object.
     * <li>Creating an <code>Area</code> from a <code>Shape</code> which encloses no
     * area (even when "closed") produces an empty <code>Area</code>. A common
     * example of this issue is that producing an <code>Area</code> from a line will
     * be empty since the line encloses no area. An empty <code>Area</code> will
     * iterate no geometry in its <code>PathIterator</code> objects.
     * <li>A self-intersecting <code>Shape</code> may be split into two (or more)
     * sub-paths each enclosing one of the non-intersecting portions of the original
     * path.
     * <li>An <code>Area</code> may take more path segments to describe the same
     * geometry even when the original outline is simple and obvious. The analysis
     * that the <code>Area</code> class must perform on the path may not reflect the
     * same concepts of "simple and obvious" as a human being perceives.
     * </ul>
     *
     * @since 1.2
     */
    class Area implements java.awt.Shape, java.lang.Cloneable {
        static EmptyCurves: java.util.Vector<any>;
        static EmptyCurves_$LI$(): java.util.Vector<any>;
        private curves;
        /**
         * The <code>Area</code> class creates an area geometry from the specified
         * {@link Shape} object. The geometry is explicitly closed, if the
         * <code>Shape</code> is not already closed. The fill rule (even-odd or
         * winding) specified by the geometry of the <code>Shape</code> is used to
         * determine the resulting enclosed area.
         *
         * @param s
         * the <code>Shape</code> from which the area is constructed
         * @throws NullPointerException
         * if <code>s</code> is null
         * @since 1.2
         */
        constructor(s?: any);
        private static pathToCurves(pi);
        /**
         * Adds the shape of the specified <code>Area</code> to the shape of this
         * <code>Area</code>. The resulting shape of this <code>Area</code> will
         * include the union of both shapes, or all areas that were contained in
         * either this or the specified <code>Area</code>.
         *
         * <pre>
         * // Example:
         * Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
         * Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
         * a1.add(a2);
         *
         * a1(before)     +         a2         =     a1(after)
         *
         * ################     ################     ################
         * ##############         ##############     ################
         * ############             ############     ################
         * ##########                 ##########     ################
         * ########                     ########     ################
         * ######                         ######     ######    ######
         * ####                             ####     ####        ####
         * ##                                 ##     ##            ##
         * </pre>
         *
         * @param rhs
         * the <code>Area</code> to be added to the current shape
         * @throws NullPointerException
         * if <code>rhs</code> is null
         * @since 1.2
         */
        add(rhs: Area): void;
        /**
         * Subtracts the shape of the specified <code>Area</code> from the shape of
         * this <code>Area</code>. The resulting shape of this <code>Area</code>
         * will include areas that were contained only in this <code>Area</code> and
         * not in the specified <code>Area</code>.
         *
         * <pre>
         * // Example:
         * Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
         * Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
         * a1.subtract(a2);
         *
         * a1(before)     -         a2         =     a1(after)
         *
         * ################     ################
         * ##############         ##############     ##
         * ############             ############     ####
         * ##########                 ##########     ######
         * ########                     ########     ########
         * ######                         ######     ######
         * ####                             ####     ####
         * ##                                 ##     ##
         * </pre>
         *
         * @param rhs
         * the <code>Area</code> to be subtracted from the current shape
         * @throws NullPointerException
         * if <code>rhs</code> is null
         * @since 1.2
         */
        subtract(rhs: Area): void;
        /**
         * Sets the shape of this <code>Area</code> to the intersection of its
         * current shape and the shape of the specified <code>Area</code>. The
         * resulting shape of this <code>Area</code> will include only areas that
         * were contained in both this <code>Area</code> and also in the specified
         * <code>Area</code>.
         *
         * <pre>
         * // Example:
         * Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
         * Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
         * a1.intersect(a2);
         *
         * a1(before)   intersect     a2         =     a1(after)
         *
         * ################     ################     ################
         * ##############         ##############       ############
         * ############             ############         ########
         * ##########                 ##########           ####
         * ########                     ########
         * ######                         ######
         * ####                             ####
         * ##                                 ##
         * </pre>
         *
         * @param rhs
         * the <code>Area</code> to be intersected with this
         * <code>Area</code>
         * @throws NullPointerException
         * if <code>rhs</code> is null
         * @since 1.2
         */
        intersect(rhs: Area): void;
        /**
         * Sets the shape of this <code>Area</code> to be the combined area of its
         * current shape and the shape of the specified <code>Area</code>, minus
         * their intersection. The resulting shape of this <code>Area</code> will
         * include only areas that were contained in either this <code>Area</code>
         * or in the specified <code>Area</code>, but not in both.
         *
         * <pre>
         * // Example:
         * Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
         * Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
         * a1.exclusiveOr(a2);
         *
         * a1(before)    xor        a2         =     a1(after)
         *
         * ################     ################
         * ##############         ##############     ##            ##
         * ############             ############     ####        ####
         * ##########                 ##########     ######    ######
         * ########                     ########     ################
         * ######                         ######     ######    ######
         * ####                             ####     ####        ####
         * ##                                 ##     ##            ##
         * </pre>
         *
         * @param rhs
         * the <code>Area</code> to be exclusive ORed with this
         * <code>Area</code>.
         * @throws NullPointerException
         * if <code>rhs</code> is null
         * @since 1.2
         */
        exclusiveOr(rhs: Area): void;
        /**
         * Removes all of the geometry from this <code>Area</code> and restores it
         * to an empty area.
         *
         * @since 1.2
         */
        reset(): void;
        /**
         * Tests whether this <code>Area</code> object encloses any area.
         *
         * @return <code>true</code> if this <code>Area</code> object represents an
         * empty area; <code>false</code> otherwise.
         * @since 1.2
         */
        isEmpty(): boolean;
        /**
         * Tests whether this <code>Area</code> consists entirely of straight edged
         * polygonal geometry.
         *
         * @return <code>true</code> if the geometry of this <code>Area</code>
         * consists entirely of line segments; <code>false</code> otherwise.
         * @since 1.2
         */
        isPolygonal(): boolean;
        /**
         * Tests whether this <code>Area</code> is rectangular in shape.
         *
         * @return <code>true</code> if the geometry of this <code>Area</code> is
         * rectangular in shape; <code>false</code> otherwise.
         * @since 1.2
         */
        isRectangular(): boolean;
        /**
         * Tests whether this <code>Area</code> is comprised of a single closed
         * subpath. This method returns <code>true</code> if the path contains 0 or
         * 1 subpaths, or <code>false</code> if the path contains more than 1
         * subpath. The subpaths are counted by the number of
         * {@link PathIterator#SEG_MOVETO SEG_MOVETO} segments that appear in the
         * path.
         *
         * @return <code>true</code> if the <code>Area</code> is comprised of a
         * single basic geometry; <code>false</code> otherwise.
         * @since 1.2
         */
        isSingular(): boolean;
        private cachedBounds;
        private invalidateBounds();
        private getCachedBounds();
        /**
         * Returns a high precision bounding {@link Rectangle2D} that completely
         * encloses this <code>Area</code>.
         * <p>
         * The Area class will attempt to return the tightest bounding box possible
         * for the Shape. The bounding box will not be padded to include the control
         * points of curves in the outline of the Shape, but should tightly fit the
         * actual geometry of the outline itself.
         *
         * @return the bounding <code>Rectangle2D</code> for the <code>Area</code>.
         * @since 1.2
         */
        getBounds2D(): java.awt.geom.Rectangle2D;
        /**
         * Returns a bounding {@link Rectangle} that completely encloses this
         * <code>Area</code>.
         * <p>
         * The Area class will attempt to return the tightest bounding box possible
         * for the Shape. The bounding box will not be padded to include the control
         * points of curves in the outline of the Shape, but should tightly fit the
         * actual geometry of the outline itself. Since the returned object
         * represents the bounding box with integers, the bounding box can only be
         * as tight as the nearest integer coordinates that encompass the geometry
         * of the Shape.
         *
         * @return the bounding <code>Rectangle</code> for the <code>Area</code>.
         * @since 1.2
         */
        getBounds(): java.awt.Rectangle;
        /**
         * Returns an exact copy of this <code>Area</code> object.
         *
         * @return Created clone object
         * @since 1.2
         */
        clone(): any;
        /**
         * Tests whether the geometries of the two <code>Area</code> objects are
         * equal. This method will return false if the argument is null.
         *
         * @param other
         * the <code>Area</code> to be compared to this <code>Area</code>
         * @return <code>true</code> if the two geometries are equal;
         * <code>false</code> otherwise.
         * @since 1.2
         */
        equals(other: Area): boolean;
        /**
         * Transforms the geometry of this <code>Area</code> using the specified
         * {@link AffineTransform}. The geometry is transformed in place, which
         * permanently changes the enclosed area defined by this object.
         *
         * @param t
         * the transformation used to transform the area
         * @throws NullPointerException
         * if <code>t</code> is null
         * @since 1.2
         */
        transform(t: java.awt.geom.AffineTransform): void;
        /**
         * Creates a new <code>Area</code> object that contains the same geometry as
         * this <code>Area</code> transformed by the specified
         * <code>AffineTransform</code>. This <code>Area</code> object is unchanged.
         *
         * @param t
         * the specified <code>AffineTransform</code> used to transform
         * the new <code>Area</code>
         * @throws NullPointerException
         * if <code>t</code> is null
         * @return a new <code>Area</code> object representing the transformed
         * geometry.
         * @since 1.2
         */
        createTransformedArea(t: java.awt.geom.AffineTransform): Area;
        contains(x?: any, y?: any, w?: any, h?: any, origrect?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double(x: number, y: number): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$java_awt_geom_Point2D(p: java.awt.geom.Point2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$double$double$double$double(x: number, y: number, w: number, h: number): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        contains$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects(x?: any, y?: any, w?: any, h?: any): any;
        /**
         * {@inheritDoc}
         *
         * @since 1.2
         */
        intersects$java_awt_geom_Rectangle2D(r: java.awt.geom.Rectangle2D): boolean;
        /**
         * Creates a {@link PathIterator} for the outline of this <code>Area</code>
         * object. This <code>Area</code> object is unchanged.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @return the <code>PathIterator</code> object that returns the geometry of
         * the outline of this <code>Area</code>, one segment at a time.
         * @since 1.2
         */
        getPathIterator$java_awt_geom_AffineTransform(at: java.awt.geom.AffineTransform): java.awt.geom.PathIterator;
        /**
         * Creates a <code>PathIterator</code> for the flattened outline of this
         * <code>Area</code> object. Only uncurved path segments represented by the
         * SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point types are returned by the
         * iterator. This <code>Area</code> object is unchanged.
         *
         * @param at
         * an optional <code>AffineTransform</code> to be applied to the
         * coordinates as they are returned in the iteration, or
         * <code>null</code> if untransformed coordinates are desired
         * @param flatness
         * the maximum amount that the control points for a given curve
         * can vary from colinear before a subdivided curve is replaced
         * by a straight line connecting the end points
         * @return the <code>PathIterator</code> object that returns the geometry of
         * the outline of this <code>Area</code>, one segment at a time.
         * @since 1.2
         */
        getPathIterator(at?: any, flatness?: any): any;
    }
    class AreaIterator implements java.awt.geom.PathIterator {
        private transform;
        private curves;
        private index;
        private prevcurve;
        private thiscurve;
        constructor(curves: java.util.Vector<any>, at: java.awt.geom.AffineTransform);
        getWindingRule(): number;
        isDone(): boolean;
        next(doNext?: any): any;
        next$(): void;
        currentSegment(coords?: any): any;
        currentSegment$double_A(coords: number[]): number;
    }
}
declare namespace java.util {
    /**
     * Maintains a last-in, first-out collection of objects. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Stack.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    class Stack<E> extends java.util.Vector<E> {
        clone(): any;
        empty(): boolean;
        peek(): E;
        pop(): E;
        push(o: E): E;
        search(o: any): number;
        constructor();
    }
}
declare namespace java.util {
    /**
     * Implementation of Map interface based on a hash table. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/HashMap.html">[Sun
     * docs]</a>
     *
     * @param <K> key type
     * @param <V> value type
     */
    abstract class AbstractHashMap<K, V> extends java.util.AbstractMap<K, V> {
        /**
         * A map of integral hashCodes onto entries.
         */
        private hashCodeMap;
        /**
         * A map of Strings onto values.
         */
        private stringMap;
        constructor(ignored?: any, alsoIgnored?: any);
        clear(): void;
        reset(): void;
        containsKey(key: any): boolean;
        containsValue(value: any): boolean;
        _containsValue(value: any, entries: java.lang.Iterable<Map.Entry<K, V>>): boolean;
        entrySet(): java.util.Set<java.util.Map.Entry<K, V>>;
        get(key: any): V;
        put(key: K, value: V): V;
        remove(key: any): V;
        size(): number;
        /**
         * Subclasses must override to return a whether or not two keys or values are
         * equal.
         */
        abstract _equals(value1: any, value2: any): boolean;
        /**
         * Subclasses must override to return a hash code for a given key. The key is
         * guaranteed to be non-null and not a String.
         */
        abstract getHashCode(key: any): number;
        /**
         * Returns the Map.Entry whose key is Object equal to <code>key</code>,
         * provided that <code>key</code>'s hash code is <code>hashCode</code>;
         * or <code>null</code> if no such Map.Entry exists at the specified
         * hashCode.
         */
        getHashValue(key: any): V;
        /**
         * Returns the value for the given key in the stringMap. Returns
         * <code>null</code> if the specified key does not exist.
         */
        getStringValue(key: string): V;
        /**
         * Returns true if the a key exists in the hashCodeMap that is Object equal to
         * <code>key</code>, provided that <code>key</code>'s hash code is
         * <code>hashCode</code>.
         */
        hasHashValue(key: any): boolean;
        /**
         * Returns true if the given key exists in the stringMap.
         */
        hasStringValue(key: string): boolean;
        /**
         * Sets the specified key to the specified value in the hashCodeMap. Returns
         * the value previously at that key. Returns <code>null</code> if the
         * specified key did not exist.
         */
        putHashValue(key: K, value: V): V;
        /**
         * Sets the specified key to the specified value in the stringMap. Returns the
         * value previously at that key. Returns <code>null</code> if the specified
         * key did not exist.
         */
        putStringValue(key: string, value: V): V;
        /**
         * Removes the pair whose key is Object equal to <code>key</code> from
         * <code>hashCodeMap</code>, provided that <code>key</code>'s hash code
         * is <code>hashCode</code>. Returns the value that was associated with the
         * removed key, or null if no such key existed.
         */
        removeHashValue(key: any): V;
        /**
         * Removes the specified key from the stringMap and returns the value that was
         * previously there. Returns <code>null</code> if the specified key does not
         * exist.
         */
        removeStringValue(key: string): V;
    }
    namespace AbstractHashMap {
        class EntrySet extends java.util.AbstractSet<Map.Entry<any, any>> {
            __parent: any;
            clear(): void;
            contains(o: any): boolean;
            iterator(): java.util.Iterator<Map.Entry<any, any>>;
            remove(index?: any): any;
            remove$java_lang_Object(entry: any): boolean;
            size(): number;
            constructor(__parent: any);
        }
        /**
         * Iterator for <code>EntrySet</code>.
         */
        class EntrySetIterator implements java.util.Iterator<Map.Entry<any, any>> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            stringMapEntries: java.util.Iterator<Map.Entry<any, any>>;
            current: java.util.Iterator<Map.Entry<any, any>>;
            last: java.util.Iterator<Map.Entry<any, any>>;
            __hasNext: boolean;
            constructor(__parent: any);
            hasNext(): boolean;
            computeHasNext(): boolean;
            next(): Map.Entry<any, any>;
            remove(): void;
        }
    }
}
declare namespace java.util {
    /**
     * Skeletal implementation of a NavigableMap.
     */
    abstract class AbstractNavigableMap<K, V> extends java.util.AbstractMap<K, V> implements java.util.NavigableMap<K, V> {
        abstract comparator(): any;
        static copyOf<K, V>(entry: Map.Entry<K, V>): Map.Entry<K, V>;
        static getKeyOrNSE<K, V>(entry: Map.Entry<K, V>): K;
        ceilingEntry(key: K): Map.Entry<K, V>;
        ceilingKey(key: K): K;
        containsKey(k: any): boolean;
        descendingKeySet(): java.util.NavigableSet<K>;
        descendingMap(): java.util.NavigableMap<K, V>;
        entrySet(): java.util.Set<Map.Entry<K, V>>;
        firstEntry(): Map.Entry<K, V>;
        firstKey(): K;
        floorEntry(key: K): Map.Entry<K, V>;
        floorKey(key: K): K;
        get(k: any): V;
        headMap(toKey?: any, inclusive?: any): any;
        headMap$java_lang_Object(toKey: K): java.util.SortedMap<K, V>;
        higherEntry(key: K): Map.Entry<K, V>;
        higherKey(key: K): K;
        keySet(): java.util.Set<K>;
        lastEntry(): Map.Entry<K, V>;
        lastKey(): K;
        lowerEntry(key: K): Map.Entry<K, V>;
        lowerKey(key: K): K;
        navigableKeySet(): java.util.NavigableSet<K>;
        pollFirstEntry(): Map.Entry<K, V>;
        pollLastEntry(): Map.Entry<K, V>;
        subMap(fromKey?: any, fromInclusive?: any, toKey?: any, toInclusive?: any): any;
        subMap$java_lang_Object$java_lang_Object(fromKey: K, toKey: K): java.util.SortedMap<K, V>;
        tailMap(fromKey?: any, inclusive?: any): any;
        tailMap$java_lang_Object(fromKey: K): java.util.SortedMap<K, V>;
        containsEntry(entry: Map.Entry<any, any>): boolean;
        /**
         * Returns an iterator over the entries in this map in descending order.
         */
        abstract descendingEntryIterator(): java.util.Iterator<Map.Entry<K, V>>;
        /**
         * Returns an iterator over the entries in this map in ascending order.
         */
        abstract entryIterator(): java.util.Iterator<Map.Entry<K, V>>;
        /**
         * Returns the entry corresponding to the specified key. If no such entry exists returns
         * {@code null}.
         */
        abstract getEntry(key: K): Map.Entry<K, V>;
        /**
         * Returns the first entry or {@code null} if map is empty.
         */
        abstract getFirstEntry(): Map.Entry<K, V>;
        /**
         * Returns the last entry or {@code null} if map is empty.
         */
        abstract getLastEntry(): Map.Entry<K, V>;
        /**
         * Gets the entry corresponding to the specified key or the entry for the least key greater than
         * the specified key. If no such entry exists returns {@code null}.
         */
        abstract getCeilingEntry(key: K): Map.Entry<K, V>;
        /**
         * Gets the entry corresponding to the specified key or the entry for the greatest key less than
         * the specified key. If no such entry exists returns {@code null}.
         */
        abstract getFloorEntry(key: K): Map.Entry<K, V>;
        /**
         * Gets the entry for the least key greater than the specified key. If no such entry exists
         * returns {@code null}.
         */
        abstract getHigherEntry(key: K): Map.Entry<K, V>;
        /**
         * Returns the entry for the greatest key less than the specified key. If no such entry exists
         * returns {@code null}.
         */
        abstract getLowerEntry(key: K): Map.Entry<K, V>;
        /**
         * Remove an entry from the tree, returning whether it was found.
         */
        abstract removeEntry(entry: Map.Entry<K, V>): boolean;
        pollEntry(entry: Map.Entry<K, V>): Map.Entry<K, V>;
        constructor();
    }
    namespace AbstractNavigableMap {
        class DescendingMap extends java.util.AbstractNavigableMap<any, any> {
            __parent: any;
            clear(): void;
            comparator(): java.util.Comparator<any>;
            descendingMap(): java.util.NavigableMap<any, any>;
            headMap(toKey?: any, inclusive?: any): any;
            put(key: any, value: any): any;
            remove(key: any): any;
            size(): number;
            subMap(fromKey?: any, fromInclusive?: any, toKey?: any, toInclusive?: any): any;
            tailMap(fromKey?: any, inclusive?: any): any;
            ascendingMap(): java.util.AbstractNavigableMap<any, any>;
            descendingEntryIterator(): java.util.Iterator<Map.Entry<any, any>>;
            entryIterator(): java.util.Iterator<Map.Entry<any, any>>;
            getEntry(key: any): Map.Entry<any, any>;
            getFirstEntry(): Map.Entry<any, any>;
            getLastEntry(): Map.Entry<any, any>;
            getCeilingEntry(key: any): Map.Entry<any, any>;
            getFloorEntry(key: any): Map.Entry<any, any>;
            getHigherEntry(key: any): Map.Entry<any, any>;
            getLowerEntry(key: any): Map.Entry<any, any>;
            removeEntry(entry: Map.Entry<any, any>): boolean;
            constructor(__parent: any);
        }
        class EntrySet extends java.util.AbstractSet<Map.Entry<any, any>> {
            __parent: any;
            contains(o: any): boolean;
            iterator(): java.util.Iterator<Map.Entry<any, any>>;
            remove(index?: any): any;
            remove$java_lang_Object(o: any): boolean;
            size(): number;
            constructor(__parent: any);
        }
        class NavigableKeySet<K, V> extends java.util.AbstractSet<K> implements java.util.NavigableSet<K> {
            forEach(action: (p1: any) => void): void;
            map: java.util.NavigableMap<K, V>;
            constructor(map: java.util.NavigableMap<K, V>);
            ceiling(k: K): K;
            clear(): void;
            comparator(): java.util.Comparator<any>;
            contains(o: any): boolean;
            descendingIterator(): java.util.Iterator<K>;
            descendingSet(): java.util.NavigableSet<K>;
            first(): K;
            floor(k: K): K;
            headSet$java_lang_Object(toElement: K): java.util.SortedSet<K>;
            headSet(toElement?: any, inclusive?: any): any;
            higher(k: K): K;
            iterator(): java.util.Iterator<K>;
            last(): K;
            lower(k: K): K;
            pollFirst(): K;
            pollLast(): K;
            remove(index?: any): any;
            remove$java_lang_Object(o: any): boolean;
            size(): number;
            subSet(fromElement?: any, fromInclusive?: any, toElement?: any, toInclusive?: any): any;
            subSet$java_lang_Object$java_lang_Object(fromElement: K, toElement: K): java.util.SortedSet<K>;
            tailSet$java_lang_Object(fromElement: K): java.util.SortedSet<K>;
            tailSet(fromElement?: any, inclusive?: any): any;
        }
        namespace NavigableKeySet {
            class NavigableKeySet$0 implements java.util.Iterator<any> {
                private entryIterator;
                __parent: any;
                forEachRemaining(consumer: (p1: any) => void): void;
                hasNext(): boolean;
                next(): any;
                remove(): void;
                constructor(__parent: any, entryIterator: any);
            }
        }
    }
}
declare namespace java.util {
    /**
     * Utility methods that operate on collections. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Collections.html">[Sun
     * docs]</a>
     */
    class Collections {
        static EMPTY_LIST: java.util.List<any>;
        static EMPTY_LIST_$LI$(): java.util.List<any>;
        static EMPTY_MAP: java.util.Map<any, any>;
        static EMPTY_MAP_$LI$(): java.util.Map<any, any>;
        static EMPTY_SET: java.util.Set<any>;
        static EMPTY_SET_$LI$(): java.util.Set<any>;
        static addAll<T>(c: java.util.Collection<any>, ...a: T[]): boolean;
        static asLifoQueue<T>(deque: java.util.Deque<T>): java.util.Queue<T>;
        /**
         * Perform a binary search on a sorted List, using a user-specified comparison
         * function.
         *
         * <p>
         * Note: The GWT implementation differs from the JDK implementation in that it
         * does not do an iterator-based binary search for Lists that do not implement
         * RandomAccess.
         * </p>
         *
         * @param sortedList List to search
         * @param key value to search for
         * @param comparator comparision function, <code>null</code> indicates
         * <i>natural ordering</i> should be used.
         * @return the index of an element with a matching value, or a negative number
         * which is the index of the next larger value (or just past the end
         * of the array if the searched value is larger than all elements in
         * the array) minus 1 (to ensure error returns are negative)
         * @throws ClassCastException if <code>key</code> and
         * <code>sortedList</code>'s elements cannot be compared by
         * <code>comparator</code>.
         */
        static binarySearch<T>(sortedList: java.util.List<any>, key: T, comparator?: java.util.Comparator<any>): number;
        static copy<T>(dest: java.util.List<any>, src: java.util.List<any>): void;
        static disjoint(c1: java.util.Collection<any>, c2: java.util.Collection<any>): boolean;
        static emptyIterator<T>(): java.util.Iterator<T>;
        static emptyList<T>(): java.util.List<T>;
        static emptyListIterator<T>(): java.util.ListIterator<T>;
        static emptyMap<K, V>(): java.util.Map<K, V>;
        static emptySet<T>(): java.util.Set<T>;
        static enumeration<T>(c: java.util.Collection<T>): java.util.Enumeration<T>;
        static fill<T>(list: java.util.List<any>, obj: T): void;
        static frequency(c: java.util.Collection<any>, o: any): number;
        static list<T>(e: java.util.Enumeration<T>): java.util.ArrayList<T>;
        static max<T>(coll: java.util.Collection<any>, comp?: java.util.Comparator<any>): T;
        static min<T>(coll: java.util.Collection<any>, comp?: java.util.Comparator<any>): T;
        static newSetFromMap<E>(map: java.util.Map<E, boolean>): java.util.Set<E>;
        static nCopies<T>(n: number, o: T): java.util.List<T>;
        static replaceAll<T>(list: java.util.List<T>, oldVal: T, newVal: T): boolean;
        static reverse<T>(l: java.util.List<T>): void;
        static reverseOrder$<T>(): java.util.Comparator<T>;
        static reverseOrder<T>(cmp?: any): any;
        /**
         * Rotates the elements in {@code list} by the distance {@code dist}
         * <p>
         * e.g. for a given list with elements [1, 2, 3, 4, 5, 6, 7, 8, 9, 0], calling rotate(list, 3) or
         * rotate(list, -7) would modify the list to look like this: [8, 9, 0, 1, 2, 3, 4, 5, 6, 7]
         *
         * @param lst the list whose elements are to be rotated.
         * @param dist is the distance the list is rotated. This can be any valid integer. Negative values
         * rotate the list backwards.
         */
        static rotate(lst: java.util.List<any>, dist: number): void;
        static shuffle<T>(list: java.util.List<T>, rnd?: java.util.Random): void;
        static singleton<T>(o: T): java.util.Set<T>;
        static singletonList<T>(o: T): java.util.List<T>;
        static singletonMap<K, V>(key: K, value: V): java.util.Map<K, V>;
        static sort<T>(target: java.util.List<T>, c?: java.util.Comparator<any>): void;
        static swap(list: java.util.List<any>, i: number, j: number): void;
        static unmodifiableCollection<T>(coll: java.util.Collection<any>): java.util.Collection<T>;
        static unmodifiableList<T>(list: java.util.List<any>): java.util.List<T>;
        static unmodifiableMap<K, V>(map: java.util.Map<any, any>): java.util.Map<K, V>;
        static unmodifiableSet<T>(set: java.util.Set<any>): java.util.Set<T>;
        static unmodifiableSortedMap<K, V>(map: java.util.SortedMap<K, any>): java.util.SortedMap<K, V>;
        static unmodifiableSortedSet<T>(set: java.util.SortedSet<any>): java.util.SortedSet<T>;
        /**
         * Computes hash code without preserving elements order (e.g. HashSet).
         */
        static hashCode$java_lang_Iterable<T>(collection: java.lang.Iterable<T>): number;
        /**
         * Computes hash code preserving collection order (e.g. ArrayList).
         */
        static hashCode<T>(list?: any): any;
        /**
         * Replace contents of a list from an array.
         *
         * @param <T> element type
         * @param target list to replace contents from an array
         * @param x an Object array which can contain only T instances
         */
        static replaceContents<T>(target: java.util.List<T>, x: any[]): void;
        static swapImpl<T>(list?: any, i?: any, j?: any): any;
        static swapImpl$java_lang_Object_A$int$int(a: any[], i: number, j: number): void;
    }
    namespace Collections {
        class LifoQueue<E> extends java.util.AbstractQueue<E> implements java.io.Serializable {
            deque: java.util.Deque<E>;
            constructor(deque: java.util.Deque<E>);
            iterator(): java.util.Iterator<E>;
            offer(e: E): boolean;
            peek(): E;
            poll(): E;
            size(): number;
        }
        class EmptyList extends java.util.AbstractList<any> implements java.util.RandomAccess, java.io.Serializable {
            contains(object: any): boolean;
            get(location: number): any;
            iterator(): java.util.Iterator<any>;
            listIterator$(): java.util.ListIterator<any>;
            size(): number;
            constructor();
        }
        class EmptyListIterator implements java.util.ListIterator<any> {
            forEachRemaining(consumer: (p1: any) => void): void;
            static INSTANCE: Collections.EmptyListIterator;
            static INSTANCE_$LI$(): Collections.EmptyListIterator;
            add(o: any): void;
            hasNext(): boolean;
            hasPrevious(): boolean;
            next(): any;
            nextIndex(): number;
            previous(): any;
            previousIndex(): number;
            remove(): void;
            set(o: any): void;
            constructor();
        }
        class EmptySet extends java.util.AbstractSet<any> implements java.io.Serializable {
            contains(object: any): boolean;
            iterator(): java.util.Iterator<any>;
            size(): number;
            constructor();
        }
        class EmptyMap extends java.util.AbstractMap<any, any> implements java.io.Serializable {
            containsKey(key: any): boolean;
            containsValue(value: any): boolean;
            entrySet(): java.util.Set<any>;
            get(key: any): any;
            keySet(): java.util.Set<any>;
            size(): number;
            values(): java.util.Collection<any>;
            constructor();
        }
        class ReverseComparator implements java.util.Comparator<java.lang.Comparable<any>> {
            static INSTANCE: Collections.ReverseComparator;
            static INSTANCE_$LI$(): Collections.ReverseComparator;
            compare(o1?: any, o2?: any): any;
            constructor();
        }
        class SetFromMap<E> extends java.util.AbstractSet<E> implements java.io.Serializable {
            backingMap: java.util.Map<E, boolean>;
            __keySet: java.util.Set<E>;
            constructor(map: java.util.Map<E, boolean>);
            add(index?: any, element?: any): any;
            add$java_lang_Object(e: E): boolean;
            clear(): void;
            contains(o: any): boolean;
            equals(o: any): boolean;
            hashCode(): number;
            iterator(): java.util.Iterator<E>;
            remove(index?: any): any;
            remove$java_lang_Object(o: any): boolean;
            size(): number;
            toString(): string;
            /**
             * Lazy initialize keySet to avoid NPE after deserialization.
             */
            keySet(): java.util.Set<E>;
        }
        class SingletonList<E> extends java.util.AbstractList<E> implements java.io.Serializable {
            element: E;
            constructor(element: E);
            contains(item: any): boolean;
            get(index: number): E;
            size(): number;
        }
        class UnmodifiableCollection<T> implements java.util.Collection<T> {
            forEach(action: (p1: any) => void): void;
            coll: java.util.Collection<any>;
            constructor(coll: java.util.Collection<any>);
            add(index?: any, element?: any): any;
            add$java_lang_Object(o: T): boolean;
            addAll(index?: any, c?: any): any;
            addAll$java_util_Collection(c: java.util.Collection<any>): boolean;
            clear(): void;
            contains(o: any): boolean;
            containsAll(c: java.util.Collection<any>): boolean;
            isEmpty(): boolean;
            iterator(): java.util.Iterator<T>;
            remove(index?: any): any;
            remove$java_lang_Object(o: any): boolean;
            removeAll(c: java.util.Collection<any>): boolean;
            retainAll(c: java.util.Collection<any>): boolean;
            size(): number;
            toArray$(): any[];
            toArray<E>(a?: any): any;
            toString(): string;
        }
        class UnmodifiableList<T> extends Collections.UnmodifiableCollection<T> implements java.util.List<T> {
            forEach(action: (p1: any) => void): void;
            list: java.util.List<any>;
            constructor(list: java.util.List<any>);
            add(index?: any, element?: any): any;
            addAll(index?: any, c?: any): any;
            equals(o: any): boolean;
            get(index: number): T;
            hashCode(): number;
            indexOf(o?: any, index?: any): any;
            indexOf$java_lang_Object(o: any): number;
            isEmpty(): boolean;
            lastIndexOf(o?: any, index?: any): any;
            lastIndexOf$java_lang_Object(o: any): number;
            listIterator$(): java.util.ListIterator<T>;
            listIterator(from?: any): any;
            remove(index?: any): any;
            set(index: number, element: T): T;
            subList(fromIndex: number, toIndex: number): java.util.List<T>;
        }
        class UnmodifiableRandomAccessList<T> extends Collections.UnmodifiableList<T> implements java.util.RandomAccess {
            constructor(list: java.util.List<any>);
        }
        class UnmodifiableSet<T> extends Collections.UnmodifiableCollection<T> implements java.util.Set<T> {
            forEach(action: (p1: any) => void): void;
            constructor(set: java.util.Set<any>);
            equals(o: any): boolean;
            hashCode(): number;
        }
        class UnmodifiableMap<K, V> implements java.util.Map<K, V> {
            __entrySet: Collections.UnmodifiableSet<java.util.Map.Entry<K, V>>;
            __keySet: Collections.UnmodifiableSet<K>;
            map: java.util.Map<any, any>;
            __values: Collections.UnmodifiableCollection<V>;
            constructor(map: java.util.Map<any, any>);
            clear(): void;
            containsKey(key: any): boolean;
            containsValue(val: any): boolean;
            entrySet(): java.util.Set<java.util.Map.Entry<K, V>>;
            equals(o: any): boolean;
            get(key: any): V;
            hashCode(): number;
            isEmpty(): boolean;
            keySet(): java.util.Set<K>;
            put(key: K, value: V): V;
            putAll(t: java.util.Map<any, any>): void;
            remove(key: any): V;
            size(): number;
            toString(): string;
            values(): java.util.Collection<V>;
        }
        namespace UnmodifiableMap {
            class UnmodifiableEntrySet<K, V> extends Collections.UnmodifiableSet<java.util.Map.Entry<K, V>> {
                constructor(s: java.util.Set<any>);
                contains(o: any): boolean;
                containsAll(o: java.util.Collection<any>): boolean;
                iterator(): java.util.Iterator<java.util.Map.Entry<K, V>>;
                toArray$(): any[];
                toArray<T>(a?: any): any;
                /**
                 * Wrap an array of Map.Entries as UnmodifiableEntries.
                 *
                 * @param array array to wrap
                 * @param size number of entries to wrap
                 */
                wrap(array: any[], size: number): void;
            }
            namespace UnmodifiableEntrySet {
                class UnmodifiableEntry<K, V> implements java.util.Map.Entry<K, V> {
                    entry: java.util.Map.Entry<any, any>;
                    constructor(entry: java.util.Map.Entry<any, any>);
                    equals(o: any): boolean;
                    getKey(): K;
                    getValue(): V;
                    hashCode(): number;
                    setValue(value: V): V;
                    toString(): string;
                }
                class UnmodifiableEntrySet$0 implements java.util.Iterator<java.util.Map.Entry<any, any>> {
                    private it;
                    __parent: any;
                    forEachRemaining(consumer: (p1: any) => void): void;
                    hasNext(): boolean;
                    next(): java.util.Map.Entry<any, any>;
                    remove(): void;
                    constructor(__parent: any, it: any);
                }
            }
        }
        class UnmodifiableSortedMap<K, V> extends Collections.UnmodifiableMap<K, V> implements java.util.SortedMap<K, V> {
            sortedMap: java.util.SortedMap<K, any>;
            constructor(sortedMap: java.util.SortedMap<K, any>);
            comparator(): java.util.Comparator<any>;
            equals(o: any): boolean;
            firstKey(): K;
            hashCode(): number;
            headMap(toKey?: any, inclusive?: any): any;
            headMap$java_lang_Object(toKey: K): java.util.SortedMap<K, V>;
            lastKey(): K;
            subMap(fromKey?: any, fromInclusive?: any, toKey?: any, toInclusive?: any): any;
            subMap$java_lang_Object$java_lang_Object(fromKey: K, toKey: K): java.util.SortedMap<K, V>;
            tailMap(fromKey?: any, inclusive?: any): any;
            tailMap$java_lang_Object(fromKey: K): java.util.SortedMap<K, V>;
        }
        class UnmodifiableSortedSet<E> extends Collections.UnmodifiableSet<E> implements java.util.SortedSet<E> {
            forEach(action: (p1: any) => void): void;
            sortedSet: java.util.SortedSet<E>;
            constructor(sortedSet: java.util.SortedSet<any>);
            comparator(): java.util.Comparator<any>;
            equals(o: any): boolean;
            first(): E;
            hashCode(): number;
            headSet(toElement?: any, inclusive?: any): any;
            headSet$java_lang_Object(toElement: E): java.util.SortedSet<E>;
            last(): E;
            subSet(fromElement?: any, fromInclusive?: any, toElement?: any, toInclusive?: any): any;
            subSet$java_lang_Object$java_lang_Object(fromElement: E, toElement: E): java.util.SortedSet<E>;
            tailSet(fromElement?: any, inclusive?: any): any;
            tailSet$java_lang_Object(fromElement: E): java.util.SortedSet<E>;
        }
        class UnmodifiableCollectionIterator<T> implements java.util.Iterator<T> {
            forEachRemaining(consumer: (p1: any) => void): void;
            it: java.util.Iterator<any>;
            constructor(it: java.util.Iterator<any>);
            hasNext(): boolean;
            next(): T;
            remove(): void;
        }
        class UnmodifiableListIterator<T> extends Collections.UnmodifiableCollectionIterator<T> implements java.util.ListIterator<T> {
            forEachRemaining(consumer: (p1: any) => void): void;
            lit: java.util.ListIterator<any>;
            constructor(lit: java.util.ListIterator<any>);
            add(o: T): void;
            hasPrevious(): boolean;
            nextIndex(): number;
            previous(): T;
            previousIndex(): number;
            set(o: T): void;
        }
        class RandomHolder {
            static rnd: java.util.Random;
            static rnd_$LI$(): java.util.Random;
        }
        class Collections$0<T> implements java.util.Enumeration<T> {
            private it;
            hasMoreElements(): boolean;
            nextElement(): T;
            constructor(it: any);
        }
        class Collections$1<T> implements java.util.Comparator<T> {
            private cmp;
            compare(t1: T, t2: T): number;
            constructor(cmp: any);
        }
    }
}
declare namespace java.util {
    /**
     * A {@link java.util.Map} of {@link Enum}s. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/EnumMap.html">[Sun
     * docs]</a>
     *
     * @param <K> key type
     * @param <V> value type
     */
    class EnumMap<K extends java.lang.Enum<K>, V> extends java.util.AbstractMap<K, V> {
        private __keySet;
        private __values;
        constructor(type?: any);
        clear(): void;
        clone(): EnumMap<K, V>;
        containsKey(key: any): boolean;
        containsValue(value: any): boolean;
        entrySet(): java.util.Set<java.util.Map.Entry<K, V>>;
        get(k: any): V;
        put(key?: any, value?: any): any;
        remove(key: any): V;
        size(): number;
        /**
         * Returns <code>key</code> as <code>K</code>. Only runtime checks that
         * key is an Enum, not that it's the particular Enum K. Should only be called
         * when you are sure <code>key</code> is of type <code>K</code>.
         */
        asKey(key: any): K;
        asOrdinal(key: any): number;
        init(type?: any): any;
        init$java_util_EnumMap(m: EnumMap<K, any>): void;
        set(ordinal: number, value: V): V;
    }
    namespace EnumMap {
        class EntrySet extends java.util.AbstractSet<Map.Entry<any, any>> {
            __parent: any;
            clear(): void;
            contains(o: any): boolean;
            iterator(): java.util.Iterator<Map.Entry<any, any>>;
            remove(index?: any): any;
            remove$java_lang_Object(entry: any): boolean;
            size(): number;
            constructor(__parent: any);
        }
        class EntrySetIterator implements java.util.Iterator<Map.Entry<any, any>> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            it: java.util.Iterator<any>;
            key: any;
            hasNext(): boolean;
            next(): Map.Entry<any, any>;
            remove(): void;
            constructor(__parent: any);
        }
        class MapEntry extends java.util.AbstractMapEntry<any, any> {
            __parent: any;
            key: any;
            constructor(__parent: any, key: any);
            getKey(): any;
            getValue(): any;
            setValue(value: any): any;
        }
    }
}
declare namespace java.util {
    /**
     * Hash table and linked-list implementation of the Set interface with
     * predictable iteration order. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/LinkedHashSet.html">[Sun
     * docs]</a>
     *
     * @param <E> element type.
     */
    class LinkedHashSet<E> extends java.util.HashSet<E> implements java.util.Set<E>, java.lang.Cloneable {
        forEach(action: (p1: any) => void): void;
        constructor(ignored?: any, alsoIgnored?: any);
        clone(): any;
    }
}
declare namespace java.io {
    /**
     * JSweet implementation for file.
     */
    class File implements java.io.Serializable, java.lang.Comparable<File> {
        /**
         * The FileSystem object representing the platform's local file system.
         */
        static fs: java.io.LocalStorageFileSystem;
        static fs_$LI$(): java.io.LocalStorageFileSystem;
        private path;
        private status;
        isInvalid(): boolean;
        private prefixLength;
        getPrefixLength(): number;
        static separatorChar: string;
        static separatorChar_$LI$(): string;
        static separator: string;
        static separator_$LI$(): string;
        static pathSeparatorChar: string;
        static pathSeparatorChar_$LI$(): string;
        static pathSeparator: string;
        static pathSeparator_$LI$(): string;
        constructor(parent?: any, child?: any, direct?: any);
        getName(): string;
        getParent(): string;
        getParentFile(): File;
        getPath(): string;
        isAbsolute(): boolean;
        getAbsolutePath(): string;
        getAbsoluteFile(): File;
        getCanonicalPath(): string;
        getCanonicalFile(): File;
        static slashify(path: string, isDirectory: boolean): string;
        canRead(): boolean;
        canWrite(): boolean;
        exists(): boolean;
        isDirectory(): boolean;
        isFile(): boolean;
        isHidden(): boolean;
        lastModified(): number;
        length(): number;
        createNewFile(): boolean;
        delete(): boolean;
        list$(): string[];
        list(filter?: any): any;
        listFiles$(): File[];
        listFiles(filter?: any): any;
        listFiles$java_io_FileFilter(filter: java.io.FileFilter): File[];
        mkdir(): boolean;
        mkdirs(): boolean;
        renameTo(dest: File): boolean;
        setLastModified(time: number): boolean;
        setReadOnly(): boolean;
        setWritable(writable: boolean, ownerOnly?: boolean): boolean;
        setReadable(readable: boolean, ownerOnly?: boolean): boolean;
        setExecutable(executable: boolean, ownerOnly?: boolean): boolean;
        canExecute(): boolean;
        static listRoots(): File[];
        getTotalSpace(): number;
        getFreeSpace(): number;
        getUsableSpace(): number;
        static createTempFile(prefix: string, suffix: string, directory?: File): File;
        compareTo(pathname?: any): any;
        equals(obj: any): boolean;
        hashCode(): number;
        toString(): string;
        static serialVersionUID: number;
    }
    namespace File {
        enum PathStatus {
            INVALID = 0,
            CHECKED = 1,
        }
        class TempDirectory {
            constructor();
            static tmpdir: java.io.File;
            static tmpdir_$LI$(): java.io.File;
            static location(): java.io.File;
            static generateFile(prefix: string, suffix: string, dir: java.io.File): java.io.File;
        }
    }
}
declare namespace java.util {
    /**
     * A helper to detect concurrent modifications to collections. This is implemented as a helper
     * utility so that we could remove the checks easily by a flag.
     */
    class ConcurrentModificationDetector {
        static API_CHECK: boolean;
        static API_CHECK_$LI$(): boolean;
        static MOD_COUNT_PROPERTY: string;
        static structureChanged(map: any): void;
        static recordLastKnownStructure(host: any, iterator: java.util.Iterator<any>): void;
        static checkStructuralChange(host: any, iterator: java.util.Iterator<any>): void;
    }
}
declare namespace java.util.logging {
    /**
     * An emulation of the java.util.logging.Logger class. See
     * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/Logger.html">
     * The Java API doc for details</a>
     */
    class Logger {
        static __static_initialized: boolean;
        static __static_initialize(): void;
        static GLOBAL_LOGGER_NAME: string;
        static LOGGING_ENABLED: string;
        static LOGGING_ENABLED_$LI$(): string;
        static LOGGING_WARNING: boolean;
        static LOGGING_WARNING_$LI$(): boolean;
        static LOGGING_SEVERE: boolean;
        static LOGGING_SEVERE_$LI$(): boolean;
        static LOGGING_FALSE: boolean;
        static LOGGING_FALSE_$LI$(): boolean;
        static __static_initializer_0(): void;
        static getGlobal(): Logger;
        static getLogger(name: string): Logger;
        static assertLoggingValues(): void;
        private handlers;
        private level;
        private name;
        private parent;
        private useParentHandlers;
        constructor(name: string, resourceName: string);
        addHandler(handler: java.util.logging.Handler): void;
        config(msg: string): void;
        fine(msg: string): void;
        finer(msg: string): void;
        finest(msg: string): void;
        info(msg: string): void;
        warning(msg: string): void;
        severe(msg: string): void;
        getHandlers(): java.util.logging.Handler[];
        getLevel(): java.util.logging.Level;
        getName(): string;
        getParent(): Logger;
        getUseParentHandlers(): boolean;
        isLoggable(messageLevel: java.util.logging.Level): boolean;
        log$java_util_logging_Level$java_lang_String(level: java.util.logging.Level, msg: string): void;
        log(level?: any, msg?: any, thrown?: any): any;
        log$java_util_logging_LogRecord(record: java.util.logging.LogRecord): void;
        removeHandler(handler: java.util.logging.Handler): void;
        setLevel(newLevel: java.util.logging.Level): void;
        setParent(newParent: Logger): void;
        setUseParentHandlers(newUseParentHandlers: boolean): void;
        private getEffectiveLevel();
        actuallyLog(level?: any, msg?: any, thrown?: any): any;
        private actuallyLog$java_util_logging_LogRecord(record);
    }
}
declare namespace javaemul.internal {
    /**
     * A utility class that provides utility functions to do precondition checks inside GWT-SDK.
     */
    class InternalPreconditions {
        static CHECKED_MODE: boolean;
        static CHECKED_MODE_$LI$(): boolean;
        static TYPE_CHECK: boolean;
        static TYPE_CHECK_$LI$(): boolean;
        static API_CHECK: boolean;
        static API_CHECK_$LI$(): boolean;
        static BOUND_CHECK: boolean;
        static BOUND_CHECK_$LI$(): boolean;
        static checkType(expression: boolean): void;
        static checkCriticalType(expression: boolean): void;
        /**
         * Ensures the truth of an expression that verifies array type.
         */
        static checkArrayType$boolean(expression: boolean): void;
        static checkCriticalArrayType$boolean(expression: boolean): void;
        /**
         * Ensures the truth of an expression that verifies array type.
         */
        static checkArrayType(expression?: any, errorMessage?: any): any;
        static checkCriticalArrayType(expression?: any, errorMessage?: any): any;
        /**
         * Ensures the truth of an expression involving existence of an element.
         */
        static checkElement$boolean(expression: boolean): void;
        /**
         * Ensures the truth of an expression involving existence of an element.
         * <p>
         * For cases where failing fast is pretty important and not failing early could cause bugs that
         * are much harder to debug.
         */
        static checkCriticalElement$boolean(expression: boolean): void;
        /**
         * Ensures the truth of an expression involving existence of an element.
         */
        static checkElement(expression?: any, errorMessage?: any): any;
        /**
         * Ensures the truth of an expression involving existence of an element.
         * <p>
         * For cases where failing fast is pretty important and not failing early could cause bugs that
         * are much harder to debug.
         */
        static checkCriticalElement(expression?: any, errorMessage?: any): any;
        /**
         * Ensures the truth of an expression involving one or more parameters to the calling method.
         */
        static checkArgument$boolean(expression: boolean): void;
        /**
         * Ensures the truth of an expression involving one or more parameters to the calling method.
         * <p>
         * For cases where failing fast is pretty important and not failing early could cause bugs that
         * are much harder to debug.
         */
        static checkCriticalArgument$boolean(expression: boolean): void;
        /**
         * Ensures the truth of an expression involving one or more parameters to the calling method.
         */
        static checkArgument$boolean$java_lang_Object(expression: boolean, errorMessage: any): void;
        /**
         * Ensures the truth of an expression involving one or more parameters to the calling method.
         * <p>
         * For cases where failing fast is pretty important and not failing early could cause bugs that
         * are much harder to debug.
         */
        static checkCriticalArgument$boolean$java_lang_Object(expression: boolean, errorMessage: any): void;
        /**
         * Ensures the truth of an expression involving one or more parameters to the calling method.
         */
        static checkArgument(expression?: any, errorMessageTemplate?: any, ...errorMessageArgs: any[]): any;
        /**
         * Ensures the truth of an expression involving one or more parameters to the calling method.
         * <p>
         * For cases where failing fast is pretty important and not failing early could cause bugs that
         * are much harder to debug.
         */
        static checkCriticalArgument(expression?: any, errorMessageTemplate?: any, ...errorMessageArgs: any[]): any;
        /**
         * Ensures the truth of an expression involving the state of the calling instance, but not
         * involving any parameters to the calling method.
         *
         * @param expression a boolean expression
         * @throws IllegalStateException if {@code expression} is false
         */
        static checkState$boolean(expression: boolean): void;
        /**
         * Ensures the truth of an expression involving the state of the calling instance, but not
         * involving any parameters to the calling method.
         * <p>
         * For cases where failing fast is pretty important and not failing early could cause bugs that
         * are much harder to debug.
         */
        static checkCritcalState(expression: boolean): void;
        /**
         * Ensures the truth of an expression involving the state of the calling instance, but not
         * involving any parameters to the calling method.
         */
        static checkState(expression?: any, errorMessage?: any): any;
        /**
         * Ensures the truth of an expression involving the state of the calling instance, but not
         * involving any parameters to the calling method.
         */
        static checkCriticalState(expression: boolean, errorMessage: any): void;
        /**
         * Ensures that an object reference passed as a parameter to the calling method is not null.
         */
        static checkNotNull$java_lang_Object<T>(reference: T): T;
        static checkCriticalNotNull$java_lang_Object<T>(reference: T): T;
        /**
         * Ensures that an object reference passed as a parameter to the calling method is not null.
         */
        static checkNotNull(reference?: any, errorMessage?: any): any;
        static checkCriticalNotNull(reference?: any, errorMessage?: any): any;
        /**
         * Ensures that {@code size} specifies a valid array size (i.e. non-negative).
         */
        static checkArraySize(size: number): void;
        static checkCriticalArraySize(size: number): void;
        /**
         * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
         * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
         */
        static checkElementIndex(index: number, size: number): void;
        static checkCriticalElementIndex(index: number, size: number): void;
        /**
         * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
         * size {@code size}. A position index may range from zero to {@code size}, inclusive.
         */
        static checkPositionIndex(index: number, size: number): void;
        static checkCriticalPositionIndex(index: number, size: number): void;
        /**
         * Ensures that {@code start} and {@code end} specify a valid <i>positions</i> in an array, list
         * or string of size {@code size}, and are in order. A position index may range from zero to
         * {@code size}, inclusive.
         */
        static checkPositionIndexes(start: number, end: number, size: number): void;
        /**
         * Ensures that {@code start} and {@code end} specify a valid <i>positions</i> in an array, list
         * or string of size {@code size}, and are in order. A position index may range from zero to
         * {@code size}, inclusive.
         */
        static checkCriticalPositionIndexes(start: number, end: number, size: number): void;
        /**
         * Checks that bounds are correct.
         *
         * @throw StringIndexOutOfBoundsException if the range is not legal
         */
        static checkStringBounds(start: number, end: number, size: number): void;
        /**
         * Substitutes each {@code %s} in {@code template} with an argument. These are matched by
         * position: the first {@code %s} gets {@code args[0]}, etc.  If there are more arguments than
         * placeholders, the unmatched arguments will be appended to the end of the formatted message in
         * square braces.
         */
        private static format(template, ...args);
        constructor();
    }
}
declare namespace java.util {
    /**
     * Implementation of Map interface based on a hash table. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/HashMap.html">[Sun
     * docs]</a>
     *
     * @param <K> key type
     * @param <V> value type
     */
    class HashMap<K, V> extends java.util.AbstractHashMap<K, V> implements java.lang.Cloneable, java.io.Serializable {
        /**
         * Ensures that RPC will consider type parameter K to be exposed. It will be
         * pruned by dead code elimination.
         */
        private exposeKey;
        /**
         * Ensures that RPC will consider type parameter V to be exposed. It will be
         * pruned by dead code elimination.
         */
        private exposeValue;
        constructor(ignored?: any, alsoIgnored?: any);
        clone(): any;
        _equals(value1: any, value2: any): boolean;
        getHashCode(key: any): number;
    }
}
declare namespace java.util {
    /**
     * Map using reference equality on keys. <a
     * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/IdentityHashMap.html">[Sun
     * docs]</a>
     *
     * @param <K> key type
     * @param <V> value type
     */
    class IdentityHashMap<K, V> extends java.util.AbstractHashMap<K, V> implements java.util.Map<K, V>, java.lang.Cloneable, java.io.Serializable {
        /**
         * Ensures that RPC will consider type parameter K to be exposed. It will be
         * pruned by dead code elimination.
         */
        private exposeKey;
        /**
         * Ensures that RPC will consider type parameter V to be exposed. It will be
         * pruned by dead code elimination.
         */
        private exposeValue;
        constructor(toBeCopied?: any);
        clone(): any;
        equals(obj: any): boolean;
        hashCode(): number;
        _equals(value1: any, value2: any): boolean;
        getHashCode(key: any): number;
    }
}
declare namespace java.util {
    /**
     * Implements a TreeMap using a red-black tree. This guarantees O(log n)
     * performance on lookups, inserts, and deletes while maintaining linear
     * in-order traversal time. Null keys and values are fully supported if the
     * comparator supports them (the default comparator does not).
     *
     * @param <K> key type
     * @param <V> value type
     */
    class TreeMap<K, V> extends java.util.AbstractNavigableMap<K, V> implements java.io.Serializable {
        static SubMapType_All: TreeMap.SubMapType;
        static SubMapType_All_$LI$(): TreeMap.SubMapType;
        static SubMapType_Head: TreeMap.SubMapType;
        static SubMapType_Head_$LI$(): TreeMap.SubMapType;
        static SubMapType_Range: TreeMap.SubMapType;
        static SubMapType_Range_$LI$(): TreeMap.SubMapType;
        static SubMapType_Tail: TreeMap.SubMapType;
        static SubMapType_Tail_$LI$(): TreeMap.SubMapType;
        static LEFT: number;
        static RIGHT: number;
        static otherChild(child: number): number;
        private cmp;
        private exposeKeyType;
        private exposeValueType;
        private root;
        private __size;
        constructor(c?: any);
        clear(): void;
        comparator(): java.util.Comparator<any>;
        entrySet(): java.util.Set<Map.Entry<K, V>>;
        headMap(toKey?: any, inclusive?: any): any;
        put(key?: any, value?: any): any;
        put$java_lang_Object$java_lang_Object(key: K, value: V): V;
        remove(k: any): V;
        size(): number;
        subMap(fromKey?: any, fromInclusive?: any, toKey?: any, toInclusive?: any): any;
        tailMap(fromKey?: any, inclusive?: any): any;
        /**
         * Returns the first node which compares greater than the given key.
         *
         * @param key the key to search for
         * @return the next node, or null if there is none
         */
        getNodeAfter(key: K, inclusive: boolean): TreeMap.Node<K, V>;
        /**
         * Returns the last node which is strictly less than the given key.
         *
         * @param key the key to search for
         * @return the previous node, or null if there is none
         */
        getNodeBefore(key: K, inclusive: boolean): TreeMap.Node<K, V>;
        /**
         * Used for testing. Validate that the tree meets all red-black correctness
         * requirements. These include:
         *
         * <pre>
         * - root is black
         * - no children of a red node may be red
         * - the black height of every path through the three to a leaf is exactly the same
         * </pre>
         *
         * @throws RuntimeException if any correctness errors are detected.
         */
        assertCorrectness$(): void;
        descendingEntryIterator(): java.util.Iterator<Map.Entry<K, V>>;
        entryIterator(): java.util.Iterator<Map.Entry<K, V>>;
        /**
         * Internal helper function for public {@link #assertCorrectness()}.
         *
         * @param tree the subtree to validate.
         * @param isRed true if the parent of this node is red.
         * @return the black height of this subtree.
         * @throws RuntimeException if this RB-tree is not valid.
         */
        assertCorrectness(tree?: any, isRed?: any): any;
        /**
         * Finds an entry given a key and returns the node.
         *
         * @param key the search key
         * @return the node matching the key or null
         */
        getEntry(key: K): Map.Entry<K, V>;
        /**
         * Returns the left-most node of the tree, or null if empty.
         */
        getFirstEntry(): Map.Entry<K, V>;
        /**
         * Returns the right-most node of the tree, or null if empty.
         */
        getLastEntry(): Map.Entry<K, V>;
        getCeilingEntry(key: K): Map.Entry<K, V>;
        getFloorEntry(key: K): Map.Entry<K, V>;
        getHigherEntry(key: K): Map.Entry<K, V>;
        getLowerEntry(key: K): Map.Entry<K, V>;
        removeEntry(entry: Map.Entry<K, V>): boolean;
        inOrderAdd(list: java.util.List<Map.Entry<K, V>>, type: TreeMap.SubMapType, current: TreeMap.Node<K, V>, fromKey: K, fromInclusive: boolean, toKey: K, toInclusive: boolean): void;
        inRange(type: TreeMap.SubMapType, key: K, fromKey: K, fromInclusive: boolean, toKey: K, toInclusive: boolean): boolean;
        /**
         * Insert a node into a subtree, collecting state about the insertion.
         *
         * If the same key already exists, the value of the node is overwritten with
         * the value from the new node instead.
         *
         * @param tree subtree to insert into
         * @param newNode new node to insert
         * @param state result of the insertion: state.found true if the key already
         * existed in the tree state.value the old value if the key existed
         * @return the new subtree root
         */
        insert(tree: TreeMap.Node<K, V>, newNode: TreeMap.Node<K, V>, state: TreeMap.State<V>): TreeMap.Node<K, V>;
        /**
         * Returns true if <code>node</code> is red. Note that null pointers are
         * considered black.
         */
        isRed(node: TreeMap.Node<K, V>): boolean;
        /**
         * Returns true if <code>a</code> is greater than or equal to <code>b</code>.
         */
        larger(a: K, b: K, orEqual: boolean): boolean;
        /**
         * Returns true if <code>a</code> is less than or equal to <code>b</code>.
         */
        smaller(a: K, b: K, orEqual: boolean): boolean;
        /**
         * Remove a key from the tree, returning whether it was found and its value.
         *
         * @param key key to remove
         * @param state return state, not null
         * @return true if the value was found
         */
        removeWithState(key: K, state: TreeMap.State<V>): boolean;
        /**
         * replace 'node' with 'newNode' in the tree rooted at 'head'. Could have
         * avoided this traversal if each node maintained a parent pointer.
         */
        replaceNode(head: TreeMap.Node<K, V>, node: TreeMap.Node<K, V>, newNode: TreeMap.Node<K, V>): void;
        /**
         * Perform a double rotation, first rotating the child which will become the
         * root in the opposite direction, then rotating the root in the specified
         * direction.
         *
         * <pre>
         * A                                               F
         * B   C    becomes (with rotateDirection=0)       A   C
         * D E F G                                         B E   G
         * D
         * </pre>
         *
         * @param tree root of the subtree to rotate
         * @param rotateDirection the direction to rotate: 0=left, 1=right
         * @return the new root of the rotated subtree
         */
        rotateDouble(tree: TreeMap.Node<K, V>, rotateDirection: number): TreeMap.Node<K, V>;
        /**
         * Perform a single rotation, pushing the root of the subtree to the specified
         * direction.
         *
         * <pre>
         * A                                              B
         * B   C     becomes (with rotateDirection=1)     D   A
         * D E                                              E   C
         * </pre>
         *
         * @param tree the root of the subtree to rotate
         * @param rotateDirection the direction to rotate: 0=left rotation, 1=right
         * @return the new root of the rotated subtree
         */
        rotateSingle(tree: TreeMap.Node<K, V>, rotateDirection: number): TreeMap.Node<K, V>;
    }
    namespace TreeMap {
        /**
         * Iterator for <code>descendingMap().entrySet()</code>.
         */
        class DescendingEntryIterator implements java.util.Iterator<Map.Entry<any, any>> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            iter: java.util.ListIterator<Map.Entry<any, any>>;
            last: Map.Entry<any, any>;
            /**
             * Create an iterator which may return only a restricted range.
             *
             * @param fromKey the first key to return in the iterator.
             * @param toKey the upper bound of keys to return.
             */
            constructor(__parent: any, type?: TreeMap.SubMapType, fromKey?: any, fromInclusive?: boolean, toKey?: any, toInclusive?: boolean);
            hasNext(): boolean;
            next(): Map.Entry<any, any>;
            remove(): void;
        }
        /**
         * Iterator for <code>EntrySet</code>.
         */
        class EntryIterator implements java.util.Iterator<Map.Entry<any, any>> {
            __parent: any;
            forEachRemaining(consumer: (p1: any) => void): void;
            iter: java.util.ListIterator<Map.Entry<any, any>>;
            last: Map.Entry<any, any>;
            /**
             * Create an iterator which may return only a restricted range.
             *
             * @param fromKey the first key to return in the iterator.
             * @param toKey the upper bound of keys to return.
             */
            constructor(__parent: any, type?: TreeMap.SubMapType, fromKey?: any, fromInclusive?: boolean, toKey?: any, toInclusive?: boolean);
            hasNext(): boolean;
            next(): Map.Entry<any, any>;
            remove(): void;
        }
        class EntrySet extends java.util.AbstractNavigableMap.EntrySet {
            __parent: any;
            clear(): void;
            constructor(__parent: any);
        }
        /**
         * Tree node.
         *
         * @param <K> key type
         * @param <V> value type
         */
        class Node<K, V> extends AbstractMap.SimpleEntry<K, V> {
            child: TreeMap.Node<K, V>[];
            isRed: boolean;
            /**
             * Create a node of the specified color.
             *
             * @param key
             * @param value
             * @param isRed true if this should be a red node, false for black
             */
            constructor(key: K, value: V, isRed?: boolean);
        }
        /**
         * A state object which is passed down the tree for both insert and remove.
         * All uses make use of the done flag to indicate when no further rebalancing
         * of the tree is required. Remove methods use the found flag to indicate when
         * the desired key has been found. value is used both to return the value of a
         * removed node as well as to pass in a value which must match (used for
         * entrySet().remove(entry)), and the matchValue flag is used to request this
         * behavior.
         *
         * @param <V> value type
         */
        class State<V> {
            done: boolean;
            found: boolean;
            matchValue: boolean;
            value: V;
            toString(): string;
            constructor();
        }
        class SubMap extends java.util.AbstractNavigableMap<any, any> {
            __parent: any;
            fromInclusive: boolean;
            fromKey: any;
            toInclusive: boolean;
            toKey: any;
            type: TreeMap.SubMapType;
            constructor(__parent: any, type: TreeMap.SubMapType, fromKey: any, fromInclusive: boolean, toKey: any, toInclusive: boolean);
            comparator(): java.util.Comparator<any>;
            entrySet(): java.util.Set<Map.Entry<any, any>>;
            headMap(toKey?: any, toInclusive?: any): any;
            isEmpty(): boolean;
            put(key?: any, value?: any): any;
            put$java_lang_Object$java_lang_Object(key: any, value: any): any;
            remove(k: any): any;
            size(): number;
            subMap(newFromKey?: any, newFromInclusive?: any, newToKey?: any, newToInclusive?: any): any;
            tailMap(fromKey?: any, fromInclusive?: any): any;
            descendingEntryIterator(): java.util.Iterator<Map.Entry<any, any>>;
            entryIterator(): java.util.Iterator<Map.Entry<any, any>>;
            getEntry(key: any): Map.Entry<any, any>;
            getFirstEntry(): Map.Entry<any, any>;
            getLastEntry(): Map.Entry<any, any>;
            getCeilingEntry(key: any): Map.Entry<any, any>;
            getFloorEntry(key: any): Map.Entry<any, any>;
            getHigherEntry(key: any): Map.Entry<any, any>;
            getLowerEntry(key: any): Map.Entry<any, any>;
            removeEntry(entry: Map.Entry<any, any>): boolean;
            guardInRange(entry: Map.Entry<any, any>): Map.Entry<any, any>;
            inRange(key: any): boolean;
        }
        namespace SubMap {
            class SubMap$0 extends TreeMap.SubMap.EntrySet {
                __parent: any;
                isEmpty(): boolean;
                constructor(__parent: any);
            }
        }
        class SubMapType {
            /**
             * Returns true if this submap type uses a from-key.
             */
            fromKeyValid(): boolean;
            /**
             * Returns true if this submap type uses a to-key.
             */
            toKeyValid(): boolean;
        }
        class SubMapTypeHead extends TreeMap.SubMapType {
            toKeyValid(): boolean;
        }
        class SubMapTypeRange extends TreeMap.SubMapType {
            fromKeyValid(): boolean;
            toKeyValid(): boolean;
        }
        class SubMapTypeTail extends TreeMap.SubMapType {
            fromKeyValid(): boolean;
        }
    }
}
declare namespace java.util {
    /**
     * Hash table implementation of the Map interface with predictable iteration
     * order. <a href=
     * "http://java.sun.com/j2se/1.5.0/docs/api/java/util/LinkedHashMap.html">[Sun
     * docs]</a>
     *
     * @param <K>
     * key type.
     * @param <V>
     * value type.
     */
    class LinkedHashMap<K, V> extends java.util.HashMap<K, V> implements java.util.Map<K, V> {
        private accessOrder;
        private head;
        private map;
        constructor(ignored?: any, alsoIgnored?: any, accessOrder?: any);
        clear(): void;
        resetChainEntries(): void;
        clone(): any;
        containsKey(key: any): boolean;
        containsValue(value: any): boolean;
        entrySet(): java.util.Set<java.util.Map.Entry<K, V>>;
        get(key: any): V;
        put(key?: any, value?: any): any;
        put$java_lang_Object$java_lang_Object(key: K, value: V): V;
        remove(key: any): V;
        size(): number;
        removeEldestEntry(eldest: java.util.Map.Entry<K, V>): boolean;
        recordAccess(entry: LinkedHashMap.ChainEntry): void;
    }
    namespace LinkedHashMap {
        /**
         * The entry we use includes next/prev pointers for a doubly-linked circular
         * list with a head node. This reduces the special cases we have to deal
         * with in the list operations.
         *
         * Note that we duplicate the key from the underlying hash map so we can
         * find the eldest entry. The alternative would have been to modify HashMap
         * so more of the code was directly usable here, but this would have added
         * some overhead to HashMap, or to reimplement most of the HashMap code here
         * with small modifications. Paying a small storage cost only if you use
         * LinkedHashMap and minimizing code size seemed like a better tradeoff
         */
        class ChainEntry extends AbstractMap.SimpleEntry<any, any> {
            __parent: any;
            next: LinkedHashMap.ChainEntry;
            prev: LinkedHashMap.ChainEntry;
            constructor(__parent: any, key?: any, value?: any);
            /**
             * Add this node to the end of the chain.
             */
            addToEnd(): void;
            /**
             * Remove this node from any list it may be a part of.
             */
            remove(): void;
        }
        class EntrySet extends java.util.AbstractSet<java.util.Map.Entry<any, any>> {
            __parent: any;
            clear(): void;
            contains(o: any): boolean;
            iterator(): java.util.Iterator<java.util.Map.Entry<any, any>>;
            remove(index?: any): any;
            remove$java_lang_Object(entry: any): boolean;
            size(): number;
            constructor(__parent: any);
        }
        namespace EntrySet {
            class EntryIterator implements java.util.Iterator<java.util.Map.Entry<any, any>> {
                __parent: any;
                forEachRemaining(consumer: (p1: any) => void): void;
                last: LinkedHashMap.ChainEntry;
                __next: LinkedHashMap.ChainEntry;
                constructor(__parent: any);
                hasNext(): boolean;
                next(): java.util.Map.Entry<any, any>;
                remove(): void;
            }
        }
    }
}
