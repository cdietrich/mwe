/*******************************************************************************
 * Copyright (c) 2008,2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.mwe2.language;

import java.util.Set;

import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.AbstractNullSafeConverter;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.util.Strings;

import com.google.common.collect.ImmutableSet;

public class Mwe2ValueConverters extends DefaultTerminalConverters {

	public class FQNConverter extends AbstractNullSafeConverter<String> {
		
		private Set<String> allKeywords = ImmutableSet.copyOf(GrammarUtil.getAllKeywords(getGrammar()));
		
		@Override
		protected String internalToValue(String string, INode node) {
			return string.replaceAll("[\\^\\s]", "");
		}

		@Override
		protected String internalToString(String value) {
			String[] segments = value.split("\\.");
			StringBuilder builder = new StringBuilder(value.length());
			boolean first = true;
			for(String segment: segments) {
				if (!first)
					builder.append('.');
				if (allKeywords.contains(segment)) {
					builder.append("^");
				}
				builder.append(segment);
				first = false;
			}
			return builder.toString();
		}
	}
	
	@ValueConverter(rule = "ImportedFQN")
	public IValueConverter<String> ImportedFQN() {
		return new FQNConverter();
	}
	
	@ValueConverter(rule = "FQN")
	public IValueConverter<String> FQN() {
		return new FQNConverter();
	}
	
	@ValueConverter(rule = "ConstantValue")
	public IValueConverter<String> ConstantValue() {
		return new AbstractNullSafeConverter<String>() {

			@Override
			protected String internalToValue(String string, INode node) {
				try {
					string = string.replace("\\${", "${");
					return Strings.convertFromJavaString(string, false);
				} catch(IllegalArgumentException e) {
					throw new ValueConverterException(e.getMessage(), node, e);
				}
			}

			@Override
			protected String internalToString(String value) {
				String result = Strings.convertToJavaString(value, false);
				result = result.replace("${", "\\${");
				return result;
			}
		};
	}
}
