const { DragDropContext } = window['ReactBeautifulDnd'];

import DayContainer from "./DayContainer";

class TaskApp extends React.Component{
	constructor(props){
		super(props);
		
		this.state = {
				taskDays: []
		}
    
    	this.stateUpdater = () => {
    		this.getLatestState();
    	}
	}

	getLatestState(){
		fetch("/tasks", {
	        "method": "GET",
	        "headers": {
	            "accept": "application/json"
	        }
	        })
	        .then(response => response.json())
	        .then(response => {
	        	this.setState({
	        		taskDays: response
	        	});
	        })
	        .catch(err => { console.log(err); });
	}

	componentDidMount(){
		this.getLatestState();
	}
	
	onDragEnd(result) {
		console.log(result);
		var tempRequest = {
			"taskId":result.draggableId,
			"destinationDate": result.destination.droppableId,
			"desiredIndex": result.destination.index
		};
		if(result.destination !== null){
			fetch("/task/priorityChange", {
		        "method": "POST",
		        "headers": {
		        	"Content-Type": "application/json",
		            "accept": "application/json"
		        },
		        body: JSON.stringify(tempRequest)
		        })
		        .then(response => {
		        	console.log(response);
		        	this.getLatestState();
		        })
		        .catch(err => { console.log(err); });
		}
	}
	
    render() {
    	const dayRender = (taskDays) => {
    		return taskDays.map(td => 
    			<DayContainer date={td.date} tasks={td.tasks} updater={this.stateUpdater} />
    		);
    	};
    	var days = this.state.taskDays.map(td => 
    		<DayContainer date={td.date} tasks={td.tasks} updater={this.stateUpdater} />
    	);
        return (
        	<DragDropContext onDragEnd={(result) => this.onDragEnd(result)}>
	            {dayRender(this.state.taskDays)}
	            </DragDropContext>
        );
    }
};

export { TaskApp as default}