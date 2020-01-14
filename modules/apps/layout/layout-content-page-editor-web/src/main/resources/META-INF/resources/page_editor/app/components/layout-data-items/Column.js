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

import classNames from 'classnames';
import React from 'react';

import {LAYOUT_DATA_ITEM_TYPES} from '../../config/constants/layoutDataItemTypes';
import Topper from '../Topper';

const Column = React.forwardRef(
	({children, className, item, layoutData}, ref) => {
		const {size} = item.config;

		return (
			<Topper
				acceptDrop={[
					LAYOUT_DATA_ITEM_TYPES.fragment,
					LAYOUT_DATA_ITEM_TYPES.row
				]}
				active={false}
				item={item}
				layoutData={layoutData}
				name={Liferay.Language.get('column')}
			>
				{() => (
					<div
						className={classNames(
							className,
							'col page-editor__col',
							{
								[`col-${size}`]: size
							}
						)}
						ref={ref}
					>
						{children}
					</div>
				)}
			</Topper>
		);
	}
);

export default Column;
