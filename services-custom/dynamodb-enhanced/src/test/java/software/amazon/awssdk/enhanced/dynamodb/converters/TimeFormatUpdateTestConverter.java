/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.enhanced.dynamodb.converters;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;
import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.TypeConvertingVisitor;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.EnhancedAttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * Test class TimeFormatUpdateTestConverter, which is used to test the AutoGeneratedTimestampRecordExtension
 * handles conversion of attributes as defined in the Convertors.
 * This class converts the timeStamp format to "dd MM yyyy HH:mm:ss" before storing to DDB.
 */
@ThreadSafe
@Immutable
public final class TimeFormatUpdateTestConverter implements AttributeConverter<Instant> {
    private static final Visitor VISITOR = new Visitor();

    public TimeFormatUpdateTestConverter() {
    }

    public static TimeFormatUpdateTestConverter create() {
        return new TimeFormatUpdateTestConverter();
    }

    @Override
    public EnhancedType<Instant> type() {
        return EnhancedType.of(Instant.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }

    @Override
    public AttributeValue transformFrom(Instant input) {
        Date myDate = Date.from(input);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = formatter.format(myDate);
        return AttributeValue.builder().s(formattedDate).build();
    }

    @Override
    public Instant transformTo(AttributeValue input) {
        try {
            if (input.s() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = formatter.parse(input.s());
                return EnhancedAttributeValue.fromString(date.toInstant().toString()).convert(VISITOR);
            }
            return EnhancedAttributeValue.fromAttributeValue(input).convert(VISITOR);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    private static final class Visitor extends TypeConvertingVisitor<Instant> {
        private Visitor() {
            super(Instant.class, TimeFormatUpdateTestConverter.class);
        }

        @Override
        public Instant convertString(String value) {
            return Instant.parse(value);
        }
    }
}
