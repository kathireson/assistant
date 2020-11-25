const { DialogTitle } = window['MaterialUI'];
const { DialogContent } = window['MaterialUI'];
const { DialogContentText } = window['MaterialUI'];
const { DialogActions } = window['MaterialUI'];
const { Button } = window['MaterialUI'];

class TwoButtonDialog extends React.Component {
	constructor(props){
		super(props);
	}
    handleAgree(){
      this.props.dialogResult(true);
    };
    handleDisagree(){
      this.props.dialogResult(false);
    };
	render(){
		return (
			<div>
			<DialogTitle id="alert-dialog-title">
            {this.props.title}
            </DialogTitle>
            <DialogContent>
            <DialogContentText id="alert-dialog-description">
              {this.props.details}
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => this.handleDisagree()} color="secondary">
              {this.props.noButton}
            </Button>
            <Button onClick={() => this.handleAgree()} color="primary" autoFocus>
            {this.props.yesButton}
            </Button>
          </DialogActions>
          </div>
		);
	}
}

export { TwoButtonDialog as default}