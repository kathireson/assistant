const { TextField } = window['MaterialUI'];
const { TableContainer } = window['MaterialUI'];
const { Table } = window['MaterialUI'];
const { TableHead } = window['MaterialUI'];
const { TableRow } = window['MaterialUI'];
const { TableCell } = window['MaterialUI'];
const { TableBody } = window['MaterialUI'];
const { IconButton } = window['MaterialUI'];
const { Icon } = window['MaterialUI'];
const { Collapse } = window['MaterialUI'];
const { Box } = window['MaterialUI'];
const { Paper } = window['MaterialUI'];


class ReportRow extends React.Component {
	constructor(props){
		super(props);
		this.state = {
			open: false
		}
	}
	
	updateState(openState){
		this.setState({
			open: openState
		});
	}
	
	render(){
		var displayDate = new Date(this.props.task.updatedTime);
		return (
			<React.Fragment>
				<TableRow>
					<TableCell>
			          <IconButton aria-label="expand row" size="small" onClick={() => this.updateState(!this.state.open)}>
			             {this.state.open? <Icon>keyboard_arrow_up</Icon> : <Icon>keyboard_arrow_down</Icon>}
			          </IconButton>
			        </TableCell>
			        <TableCell component="th" scope="row">{this.props.task.title}</TableCell>
			        <TableCell align="right">{this.props.task.status}</TableCell>
				</TableRow>
				<TableRow>
		        	<TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
		        		<Collapse in={this.state.open} timeout="auto" unmountOnExit>
		        			<Box margin={1}>
		        				<p className="collapsibleDetail">Description : {this.props.task.description} </p>
		        				<p className="collapsibleDetail">last updated on : {displayDate.toLocaleDateString("en-us")}, {displayDate.toLocaleTimeString("en-us")}</p>
		        			</Box>
		        		</Collapse>
		        	</TableCell>
		        </TableRow>
			</React.Fragment>
		);
	}
};

class ReportApp extends React.Component {
	constructor(props){
		super(props);
		this.state = {
			date : "",
			tasks : []
		};
	}
	handleChange(event){
		this.setState({
			date : event.target.value
		});
		this.getLatestTasks(event.target.value);
	}
	getLatestTasks(date){
		var uri = "/task/report?forDate=" + date;
		fetch(uri, {
	        "method": "GET",
	        "headers": {
	            "accept": "application/json"
	        }
	        })
	        .then(response => response.json())
	        .then(response => {
	        	this.setState({
	        		tasks : response
	        	});
	        })
	        .catch(err => { console.log(err); });
	}
	render(){
		var rows = this.state.tasks.map((task) => 
			<ReportRow task={task} />
		);
		return(
			<div className="day">
				<TextField
			    id="date"
			    label="Date for Report"
			    type="date"
			    name="date"
			    defaultValue="2020-11-24"
			    value={this.state.date}
				onChange={(event) => {this.handleChange(event)}}
			    variant="outlined"
			    InputLabelProps={{
			      shrink: true,
			    }} />
				<div className="dayReport">
					<TableContainer component={Paper}>
				      <Table aria-label="collapsible table">
				        <TableHead>
				          <TableRow>
				            <TableCell />
				            <TableCell>Task Title</TableCell>
				            <TableCell align="right">Status</TableCell>
				          </TableRow>
				        </TableHead>
				        <TableBody>
				        	{rows}
				        </TableBody>
				      </Table>
				    </TableContainer>
				</div>
			</div>
		);
	}
};

export { ReportApp as default }