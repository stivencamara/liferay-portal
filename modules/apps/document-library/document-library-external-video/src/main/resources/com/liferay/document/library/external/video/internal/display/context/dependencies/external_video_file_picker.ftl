function ExternalVideoPicker(callback) {
	callback();
}

ExternalVideoPicker.prototype = {
	constructor: ExternalVideoPicker,

	openPicker: function() {
		const url = prompt('Video URL')
		if (url) {
			fetch(
				'${getDLExternalVideoFieldsURL}&${namespace}dlExternalVideoURL=' + url
			)
			.then(
				res => res.json()
			)
			.then(
				${onFilePickCallback}
			);
		}
	}
};