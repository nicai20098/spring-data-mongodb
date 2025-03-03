/*
 * Copyright 2008-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mongodb.util.json;

import static java.lang.String.*;

import org.bson.BsonDouble;
import org.bson.json.JsonParseException;
import org.bson.types.Decimal128;

/**
 * JsonToken implementation borrowed from <a href=
 * "https://github.com/mongodb/mongo-java-driver/blob/master/bson/src/main/org/bson/json/JsonToken.java">MongoDB
 * Inc.</a> licensed under the Apache License, Version 2.0. <br />
 *
 * @author Jeff Yemin
 * @author Ross Lawley
 * @since 2.2
 */
class JsonToken {

	private final Object value;
	private final JsonTokenType type;

	JsonToken(final JsonTokenType type, final Object value) {

		this.value = value;
		this.type = type;
	}

	Object getValue() {
		return value;
	}

	<T> T getValue(final Class<T> clazz) {

		try {
			if (Long.class == clazz) {
				if (value instanceof Integer) {
					return clazz.cast(((Integer) value).longValue());
				} else if (value instanceof String) {
					return clazz.cast(Long.valueOf((String) value));
				}
			} else if (Integer.class == clazz) {
				if (value instanceof String) {
					return clazz.cast(Integer.valueOf((String) value));
				}
			} else if (Double.class == clazz) {
				if (value instanceof String) {
					return clazz.cast(Double.valueOf((String) value));
				}
			} else if (Decimal128.class == clazz) {
				if (value instanceof Integer) {
					return clazz.cast(new Decimal128((Integer) value));
				} else if (value instanceof Long) {
					return clazz.cast(new Decimal128((Long) value));
				} else if (value instanceof Double) {
					return clazz.cast(new BsonDouble((Double) value).decimal128Value());
				} else if (value instanceof String) {
					return clazz.cast(Decimal128.parse((String) value));
				}
			}

			return clazz.cast(value);
		} catch (Exception e) {
			throw new JsonParseException(format("Exception converting value '%s' to type %s", value, clazz.getName()), e);
		}
	}

	public JsonTokenType getType() {
		return type;
	}
}
