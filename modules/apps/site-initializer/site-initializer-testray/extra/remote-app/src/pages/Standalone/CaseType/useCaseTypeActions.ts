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

import useFormModal from '../../../hooks/useFormModal';
import useMutate from '../../../hooks/useMutate';
import i18n from '../../../i18n';
import {Security} from '../../../security';
import {TestrayCaseType, deleteResource} from '../../../services/rest';
import {Action} from '../../../types';

const useCaseTypeActions = () => {
	const {removeItemFromList} = useMutate();

	const formModal = useFormModal();
	const modal = formModal.modal;

	const actions: Action[] = [
		{
			action: (item: TestrayCaseType) => modal.open(item),
			name: i18n.translate('edit'),
			permission: 'UPDATE',
		},
		{
			action: ({id}: TestrayCaseType, mutate) =>
				deleteResource(`/casetypes/${id}`)
					.then(() => removeItemFromList(mutate, id))
					.then(modal.onSave)
					.catch(modal.onError),
			name: i18n.translate('delete'),
			permission: 'DELETE',
		},
	];

	return {
		actions: (row: any) => Security.filterActions(actions, row.actions),
		formModal,
	};
};

export default useCaseTypeActions;
