const { TextField } = window['MaterialUI'];
const { FormControl } = window['MaterialUI'];
const { Button } = window['MaterialUI'];
const { InputLabel } = window['MaterialUI'];
const { Switch } = window['MaterialUI'];
const { Select } = window['MaterialUI'];
const { MenuItem } = window['MaterialUI'];
const { FormControlLabel } = window['MaterialUI'];
const { createMuiTheme } = window['MaterialUI'];
const { MuiThemeProvider } = window['MaterialUI'];

class TaskForm extends React.Component {
	constructor(props){
		super(props);
		this.defaultTaskDetail = {
			id : "",
			title : "",
			description : "",
			status : "",
			current : false,
			tags : "",
			createdDate : props.date
		};
		this.state = props.taskDetail !== undefined ? props.taskDetail : this.defaultTaskDetail;
	};


	handleCurrentChange(event){
		this.setState({
			[event.target.name]: event.target.checked
		})
	}
	handleChange(event){
		this.setState({
			[event.target.name]: event.target.value
		})
	}
	createTask(){
		var tempRequest = Object.assign({}, this.state);
		delete tempRequest.id;
		if(tempRequest.tags){
			tempRequest.tags = tempRequest.tags.split("|");
		}
		if(tempRequest.tags === ""){
			delete tempRequest.tags;
		}
		console.log(tempRequest);
		fetch("/task", {
	        "method": "POST",
	        "headers": {
	        	"Content-Type": "application/json",
	            "accept": "application/json"
	        },
	        body: JSON.stringify(tempRequest)
	        })
	        .then(response => response.json())
	        .then(response => {
	        	this.props.onSubmit();
	        })
	        .catch(err => { console.log(err); });
	}
	updateTask(){
		var tempRequest = Object.assign({}, this.state);
		if(tempRequest.tags){
			tempRequest.tags = tempRequest.tags.split("|");
		}
		if(tempRequest.tags === ""){
			delete tempRequest.tags;
		}
		var uri = "/task/" + this.props.taskDetail.id;
		fetch(uri, {
	        "method": "PUT",
	        "headers": {
	        	"Content-Type": "application/json",
	            "accept": "application/json"
	        },
	        body: JSON.stringify(tempRequest)
	        })
	        .then(response => {
	        	this.props.onSubmit();
	        })
	        .catch(err => { console.log(err); });
	}
	handleSubmit(){
		if(this.props.taskDetail !== undefined){
			this.updateTask();
		} else {
			this.createTask();
		}
	}
	
	render(){
		const theme = createMuiTheme({
			  overrides: {
			    MuiFormControl: {
			      root: {
			        margin: "10px",
			      }
			    }
			  }
			});
		const resetChange = (event) => {
			this.setState( prevState => {
				return this.defaultTaskDetail;
			});
		  };
		return (
			<div className = "taskForm">
			<MuiThemeProvider theme={theme}>
				<FormControl>
				  <TextField required 
				  	id="title"
				    name="title"
				  	label="Title" 
				  	variant="outlined" 
				  	value={this.state.title} 
				    onChange={(event => {this.handleChange(event)})}/>

				  <TextField 
				  	id="desc"
				    name="description"
				  	label="Description"
				  	multiline rows={4}
				    variant="outlined" 
				    value={this.state.description}
				    onChange={(event => {this.handleChange(event)})}/>

				  <FormControl variant="outlined" >
				  	<InputLabel id="Status-Label">Status</InputLabel>
				    <Select
			          labelId="Status-Label"
			          id="status"
			          name="status"
			          label="Status"
			          value={this.state.status}
				      onChange={(event => {this.handleChange(event)})}
			        >
			          <MenuItem value={"BACKLOG"}>Backlog</MenuItem>
			          <MenuItem value={"IN_PROGRESS"}>In Progress</MenuItem>
			          <MenuItem value={"DONE"}>Done</MenuItem>
			      </Select>
				 </FormControl>

				 <FormControl variant="outlined" >
				      <FormControlLabel
				        label="Current?"
				        control={
				          <Switch
				            checked={this.state.current}
				            onChange={(event => {this.handleCurrentChange(event)})}
				            name="current"
				            color="primary"
				          />
				        }
				      />
			      </FormControl>

				  <TextField
				    id="date"
				    label="Date"
				    type="date"
				    name="createdDate"
				    defaultValue="2020-11-24"
				    variant="outlined"
				    value={this.state.createdDate}
				    onChange={(event) => {this.handleChange(event)}}
				    InputLabelProps={{
				      shrink: true,
				    }} />

				  <TextField required 
				  	id="tags"
				    name="tags"
				  	label="Tags" 
				  	variant="outlined" 
				  	value={this.state.tags} 
				    onChange={(event => {this.handleChange(event)})}/>
				  
				  <div className="formButtons">
					  <Button variant="contained" color="primary" onClick={() => {this.handleSubmit()}} disableElevation>Submit</Button>
					  <Button variant="contained" color="secondary" onClick={() => {resetChange()}} disableElevation>Reset</Button>
				  </div>

				</FormControl>
				</MuiThemeProvider>
			</div>
		);
	}
}

export { TaskForm as default}