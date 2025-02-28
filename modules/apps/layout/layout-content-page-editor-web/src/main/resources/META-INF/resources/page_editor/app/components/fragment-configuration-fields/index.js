/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {LengthField} from '../../../common/components/LengthField';
import {ButtonGroupField} from './ButtonGroupField';
import CSSClassSelectorField from './CSSClassSelectorField';
import {CategoryTreeNodeSelectorField} from './CategoryTreeNodeSelectorField';
import {CheckboxField} from './CheckboxField';
import {CollectionSelectorField} from './CollectionSelectorField';
import {ColorPaletteField} from './ColorPaletteField';
import {ColorPickerField} from './ColorPickerField';
import CustomCSSField from './CustomCSSField';
import {HideFragmentField} from './HideFragmentField';
import {ImageSelectorField} from './ImageSelectorField';
import {ItemSelectorField} from './ItemSelectorField';
import {NavigationMenuSelectorField} from './NavigationMenuSelectorField';
import {SelectField} from './SelectField';
import {SpacingBoxField} from './SpacingBoxField';
import {TextField} from './TextField';
import {VideoSelectorField} from './VideoSelectorField';

export const FRAGMENT_CONFIGURATION_FIELDS = {
	buttonGroup: Liferay.FeatureFlags['LPS-143206']
		? ButtonGroupField
		: SelectField,
	categoryTreeNodeSelector: CategoryTreeNodeSelectorField,
	checkbox: CheckboxField,
	collectionSelector: CollectionSelectorField,
	colorPalette: ColorPaletteField,
	colorPicker: ColorPickerField,
	cssClassSelector: CSSClassSelectorField,
	customCSS: CustomCSSField,
	hideFragment: HideFragmentField,
	imageSelector: ImageSelectorField,
	itemSelector: ItemSelectorField,
	length: Liferay.FeatureFlags['LPS-143206'] ? LengthField : TextField,
	navigationMenuSelector: NavigationMenuSelectorField,
	select: SelectField,
	spacing: SpacingBoxField,
	text: TextField,
	videoSelector: VideoSelectorField,
};
