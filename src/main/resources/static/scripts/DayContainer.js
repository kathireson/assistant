const { Button } = window['MaterialUI'];
const { Icon } = window['MaterialUI'];
const { Modal } = window['MaterialUI'];
const { Droppable } = window['ReactBeautifulDnd'];

import TaskForm from "./TaskForm";
import Task from "./Task";

class DayContainer extends React.Component{
	constructor(props){
		super(props);
		this.state = {
			open: false
		};
	}
	
	handleOpen(){
	    this.setState({
	    	open: true
	    });
	  };

	handleClose(){
		this.setState({
	    	open: false
	    });
		this.props.updater();
	};
	
	render() {
		var tasks = this.props.tasks.map( (t, index) => 
			<Task title={t.title} 
				description={t.description}
				status={t.status}
				current={t.current}
				id={t.id}
				createdDate={this.props.date}
				updater={this.props.updater}
				tags={t.tags}
				index={index} />
		);
		return (
			<Droppable droppableId={this.props.date} >
			{(provided, snapshot) => (
					<div className="day" 
						ref={provided.innerRef}
						{...provided.droppableProps}>
						<div className = "dayHeader">
							<Icon onClick={ () => this.handleOpen()}>add_circle_outline</Icon>
							<div className = "taskDate"> {this.props.date} </div>
						</div>
						<div className = "taskContainer">
							{tasks}
						</div>
				      <Modal
				        open={this.state.open}
				        onClose={() => this.handleClose() }
				        aria-labelledby="simple-modal-title"
				        aria-describedby="simple-modal-description"
				        appElement={document.getElementById('root')}
				      >
				      	<div>
				      		<TaskForm date={this.props.date} onSubmit={() => {this.handleClose()}} />
				        </div>
				      </Modal>
				      {provided.placeholder}
				</div>
				)}
			</Droppable>
		);
	}
};

export { DayContainer as default}