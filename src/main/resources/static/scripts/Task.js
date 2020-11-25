const { Button } = window['MaterialUI'];
const { Icon } = window['MaterialUI'];
const { Modal } = window['MaterialUI'];
const { Dialog } = window['MaterialUI'];
const { Draggable } = window['ReactBeautifulDnd'];

import TaskForm from "./TaskForm";
import TwoButtonDialog from "./TwoButtonDialog";

class Task extends React.Component {
	constructor(props){
		super(props);
		this.state = {
			editOpen: false,
			dialogOpen:false
		};
	}
	dialogResult(okToDelete){
		if(okToDelete){
			this.deleteTask();
		}
		this.handleClose();
	}
	handleOpen(){
	    this.setState({
	    	["editOpen"]: true
	    });
	  };
	handleDialogOpen(){
	    this.setState({
	    	["dialogOpen"]: true
	    });
	  };
	handleClose(){
		this.setState({
	    	["editOpen"]: false,
	    	["dialogOpen"]: false
	    });
		this.props.updater();
	};
	deleteTask(){
		var uri = "/task/" + this.props.id;
		fetch(uri, {
	        "method": "DELETE",
	        "headers": {
	        	"Content-Type": "application/json",
	            "accept": "application/json"
	        }})
	        .then(response => {
	        	this.props.updater();
	        })
	        .catch(err => { console.log(err); });
	}
	markCurrent(){
		var uri = "/task/" + this.props.id + "/current";
		fetch(uri, {
	        "method": "POST",
	        "headers": {
	        	"Content-Type": "application/json",
	            "accept": "application/json"
	        }})
	        .then(response => {
	        	this.props.updater();
	        })
	        .catch(err => { console.log(err); });
	}
	markDone(){
		var uri = "/task/" + this.props.id + "/done";
		fetch(uri, {
	        "method": "POST",
	        "headers": {
	        	"Content-Type": "application/json",
	            "accept": "application/json"
	        }})
	        .then(response => {
	        	this.props.updater();
	        })
	        .catch(err => { console.log(err); });
	}
	render(){
		var currentButton = " ";
		if( this.props.current == false){
			currentButton = <Button variant="contained" onClick={() => this.markCurrent()} disableElevation>Current</Button>
		} else {
			currentButton = <Button variant="contained" color="primary" onClick={() => this.markDone()} disableElevation>Done</Button>
		}
		var currentTask = this.props.current ? "task-current": "task";
		
		// task to be passed to edit window
		var taskDetail = {
				id : this.props.id,
				title : this.props.title,
				description : this.props.description,
				status : this.props.status,
				current : this.props.current,
				createdDate : this.props.createdDate
			};
		return (
				<Draggable key={this.props.id} draggableId={this.props.id} index={this.props.index} >
				{(provided, snapshot) =>(
						<div className = {currentTask}
							ref={provided.innerRef}
					      {...provided.draggableProps} >
						<div className ="taskDetails" >
				            <div className ="taskTitle" {...provided.dragHandleProps}>{this.props.title}</div>
				            <div className ="taskQuickEdit">
					            <span className ="taskStatus">{this.props.status}</span>
					            <Icon onClick={() => this.handleOpen()}><a href="#">edit</a></Icon>
					            <Icon onClick={() => this.handleDialogOpen()}><a href="#">delete_forever</a></Icon>
				            </div>
				        </div>
				        <div className ="taskDesc"><span>{this.props.description}</span>
					         {currentButton}
				        </div>
					      <Modal
					        open={this.state.editOpen}
					        onClose={() => this.handleClose() }
					        aria-labelledby="simple-modal-title"
					        aria-describedby="simple-modal-description"
					        appElement={document.getElementById('root')}
					      >
					      	<div>
					      		<TaskForm date={this.props.date} taskDetail={taskDetail} onSubmit={() => {this.handleClose()}} />
					        </div>
					      </Modal>
					      <div>
							<Dialog
					          open={this.state.dialogOpen}
					          onClose={() => this.handleClose()}
					          aria-labelledby="alert-dialog-title"
					          aria-describedby="alert-dialog-description"
					        >
							<TwoButtonDialog 
								title="Confirm Delete"
								details="Do you really want to delete?"
								yesButton="Yes"
								noButton="No"
								dialogResult={(result) => this.dialogResult(result)} />
							</Dialog>
					      </div>
					</div>
				)}
				</Draggable>
		);
	}
}

export { Task as default}